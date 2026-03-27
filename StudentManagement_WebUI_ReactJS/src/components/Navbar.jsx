import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import '../css/Navbar.css'
import profileIcon from '../assets/icons/userIcon.png'


function Navbar() {

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userId");
  }

  const role = localStorage.getItem('role');

  if (role === 'STUDENT') {
    return (
      <nav className="navbar">
        <div className='nav-left'>
          <Link to="/" className='nav-link'>Home</Link>
          <Link to={`/students/${localStorage.getItem('userId')}`} className='nav-link'>Profile</Link>
        </div>

        <div className='nav-right'>
          <div className='profile-icon'><Link to={`/students/${localStorage.getItem('userId')}`}> <img src={profileIcon} alt="profile"/> </Link></div>
          <Link to="/auth/login" onClick={logout} className='nav-link'>Logout</Link>
        </div>
        
      </nav>
    );
  }
  
  return (
    <nav className="navbar">
      <div className='nav-left'>
        <Link to="/" className='nav-link'>Home</Link>
        <Link to="/register" className='nav-link'>Register</Link>
        <Link to="/notifications" className='nav-link'> Notifications</Link>
      </div>

      <div className='nav-right'>
        <div className='profile-icon'><img src={profileIcon} alt="profile"/></div>
        <Link to="/auth/login" onClick={logout} className='nav-link'>Logout</Link>
      </div>
      
    </nav>
  );
}

export default Navbar;