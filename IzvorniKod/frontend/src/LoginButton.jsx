import React from 'react';

function LoginButton({ onClick }) {
  return (
    <button className="open-modal-button" onClick={onClick}>
      Login
    </button>
  );
}

export default LoginButton;