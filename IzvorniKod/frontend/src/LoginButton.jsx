import React from 'react';

function LoginButton({ onClick }) {  //element kreirra gumb koji poziva s propom funkciju koja u appu mijenja stanje
  return (
    <button className="open-modal-button" onClick={onClick}>
      Login
    </button>
  );
}

export default LoginButton;