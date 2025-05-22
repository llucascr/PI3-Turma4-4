/**
 * Import function triggers from their respective submodules:
 *
 * import {onCall} from "firebase-functions/v2/https";
 * import {onDocumentWritten} from "firebase-functions/v2/firestore";
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

import {onRequest} from "firebase-functions/v2/https";
import * as logger from "firebase-functions/logger";

// Start writing functions
// https://firebase.google.com/docs/functions/typescript

import * as admin from "firebase-admin";
import * as crypto from "crypto";
import * as QRCode from "qrcode";

admin.initializeApp();
const firestore = admin.firestore();

export const performAuth = onRequest(async (request, response) => {
  try {
    const { siteUrl, apiKey } = request.body;

    if (!siteUrl || !apiKey) {
      logger.warn("Parâmetros obrigatórios ausentes.");
      response.status(400).send("Parâmetros obrigatórios: siteUrl e apiKey.");
      return;
    }

    const partnerDoc = await firestore.collection("partners").doc(siteUrl).get();
    if (!partnerDoc.exists || partnerDoc.data()?.apiKey !== apiKey) {
      logger.warn("Parceiro não encontrado ou apiKey inválida.");
      response.status(403).send("Acesso negado.");
      return;
    }

    const loginToken = crypto.randomBytes(128).toString("hex"); // 256 caracteres

    await firestore.collection("login").add({
      siteUrl,
      apiKey,
      loginToken,
      createdAt: admin.firestore.FieldValue.serverTimestamp(),
      attempts: 0
    });

    const qrCodeBase64 = await QRCode.toDataURL(loginToken);

    logger.info("QR Code gerado com sucesso para:", siteUrl);
    response.status(200).json({ qrCodeBase64 });
  } catch (error) {
    logger.error("Erro em performAuth:", error);
    response.status(500).send("Erro interno no servidor.");
  }
});

export const getLoginStatus = onRequest(async (request, response) => {
  try {
    const { loginToken } = request.body;

    if (!loginToken) {
      logger.warn("Token ausente na requisição.");
      response.status(400).send("Parâmetro obrigatório: loginToken.");
      return;
    }

    const querySnapshot = await firestore.collection("login")
      .where("loginToken", "==", loginToken)
      .limit(1)
      .get();

    if (querySnapshot.empty) {
      logger.warn("Token não encontrado.");
      response.status(404).send("Token inválido ou expirado.");
      return;
    }

    const doc = querySnapshot.docs[0];
    const data = doc.data();

    const now = admin.firestore.Timestamp.now();
    const createdAt = data.createdAt;
    const attempts = data.attempts ?? 0;

    // Verificar tempo de vida (1 minuto)
    const diffSeconds = (now.seconds - createdAt.seconds);
    if (diffSeconds > 60 || attempts >= 3) {
      await doc.ref.delete(); // excluir token inválido
      logger.warn("Token expirado ou tentativas excedidas.");
      response.status(403).send("Token expirado. Gere um novo QR Code.");
      return;
    }

    // Atualizar tentativas
    await doc.ref.update({ attempts: attempts + 1 });

    // Verifica se já foi usado
    if (data.user) {
      logger.info("Login confirmado para usuário:", data.user);
      response.status(200).json({
        user: data.user,
        loginTime: data.userLoggedAt.toDate()
      });
    } else {
      logger.info("Token ainda não utilizado.");
      response.status(204).send(); // No Content
    }

  } catch (error) {
    logger.error("Erro em getLoginStatus:", error);
    response.status(500).send("Erro interno no servidor.");
  }
});
