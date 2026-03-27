import AdminHome from './AdminHome';
import Home from './Home';

function HomePage() {
  const role = localStorage.getItem('role');
  if (role === 'ADMIN') {
    return <AdminHome />;
  } else {
    return <Home />;
  }
}

export default HomePage;