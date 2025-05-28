const PROJECT_ID = "superid-pi3-turma4-4";
const API_KEY = "AlzaSyDsbcDQJH5N7_AysjnVJDw_qkcQkjfPz20"
const SITE_URL = "www.example.com";

let loginToken = null;
let pollInterval = null;
let pollCount = 0;
const MAX_POLLS = 10;
const POLL_INTERVAL_MS = 3000;

function openModal() {
  document.getElementById("modal").style.display = "flex";
  generateQRCode();
}

function closeModal() {
  document.getElementById("modal").style.display = "none";
  clearInterval(pollInterval);
  document.getElementById("status").innerText = "Aguardando escaneamento...";
  document.getElementById("qrcode").src = "";
}

async function generateQRCode() {
  try {
    const res = await fetch(`http://127.0.0.1:5001/superid-pi3-turma4-4/us-central1/performAuth`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        apiKey: API_KEY,
        url: SITE_URL
      })
    });

    const data = await res.json();
    loginToken = data.loginToken;

    document.getElementById("qrcode").src = data.qrBase64;
    document.getElementById("status").innerText = "Escaneie com o app SuperID";

    pollCount = 0;
    if (pollInterval) clearInterval(pollInterval);
    pollInterval = setInterval(checkLoginStatus, POLL_INTERVAL_MS);

  } catch (err) {
    document.getElementById("status").innerText = "Erro ao gerar QR Code";
    console.error(err);
  }
}

async function checkLoginStatus() {
  try {
    const res = await fetch(`http://127.0.0.1:5001/superid-pi3-turma4-4/us-central1/getLoginStatus`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ loginToken })
    });

    const data = await res.json();

    if (data.status === "success") {
      clearInterval(pollInterval);
      showSuccessMessage()
    } else if (data.status === "expired") {
      clearInterval(pollInterval);
      document.getElementById("status").innerText = "Token expirado. Gerando novo QR Code...";
      setTimeout(generateQRCode, 1000);
    } else {
      pollCount++;
      if (pollCount >= MAX_POLLS) {
        clearInterval(pollInterval);
        document.getElementById("status").innerText = "Tempo esgotado. Gerando novo QR Code...";
        setTimeout(generateQRCode, 1000);
      }
    }

  } catch (err) {
    clearInterval(pollInterval);
    document.getElementById("status").innerText = "Erro ao verificar status";
    console.error(err);
  }
}

function showSuccessMessage(message = "✅ Login efetuado com sucesso!") {
    const snackbar = document.getElementById("snackbar");
    snackbar.innerText = message;
    snackbar.classList.add("show");

    setTimeout(() => {
        snackbar.classList.remove("show");
        closeModal();
    }, 3000);
}