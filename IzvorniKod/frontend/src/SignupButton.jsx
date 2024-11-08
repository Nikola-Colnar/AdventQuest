import React from 'react';

function SignupButton({ onClick }) {  //element kreirra gumb koji poziva s propom funkciju koja u appu mijenja stanje
  return (
    <button className="open-modal-button" onClick={onClick}>
      Sign up
    </button>
  );
}

export default SignupButton;