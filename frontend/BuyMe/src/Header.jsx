import React from 'react';
import { CartIcon } from './CartIcon';
import { ProfileDropdown } from './ProfileDropDown';
import { SearchBar } from './SearchBar';
import './assets/style.css';
import Logo from './Logo';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faShop } from '@fortawesome/free-solid-svg-icons'; // Import shop icon

export function Header({ cartCount, username, onSearch }) {
  const navigate = useNavigate();

  const handleBecomeSeller = () => {
    navigate('/seller-registration');
  };

  return (
    <header className="header">
      <div className="header-content">
        <Logo />
        <SearchBar onSearch={onSearch} />
        <div className="header-actions">
          <CartIcon count={cartCount} />
          <ProfileDropdown username={username} />
          <button
            className="become-seller-btn"
            onClick={handleBecomeSeller}
          >
            <FontAwesomeIcon icon={faShop} className="shop-icon" /> {/* Shop Icon */}
            Become a Seller
          </button>
        </div>
      </div>
    </header>
  );
}
