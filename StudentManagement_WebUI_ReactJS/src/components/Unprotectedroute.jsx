import { Navigate, Outlet, useLocation } from "react-router-dom";


function Unprotectedroute(){
    const token = localStorage.getItem("token")
    const location  = useLocation();

    if(token){
        return <Navigate to="/" state={{from: location}} replace />
    }

    return (
        <>
            
            <Outlet />
            
        </>
    );
}

export default Unprotectedroute;