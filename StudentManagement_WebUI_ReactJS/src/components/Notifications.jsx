import { fetchNotifications } from "./FetchNotifications";
import { useQuery } from "@tanstack/react-query";
import "../css/Notification.css"

function Notifications(){
    const {data: notificationData, isLoading,isError, error} = useQuery({
        queryKey:["notifications"],
        queryFn: fetchNotifications,
    });

    if (isLoading) return <p style={{textAlign:'center'}}> Loading notifications...</p>
    if (isError) return <p  style={{textAlign:'center'}}>Failed to load notifications!</p>

    return (
        <div>
            <h1 className="heading-bar">Notifications</h1>
            <div className="scrollable-container">
            <ul>
              {notificationData?.map(notification => (
                
                    <li key={notification.id} className="list-style">
                        <div className="notification-text">
                        <span className="meta-text">
                            {notification.studentId}
                        </span>
                        
                            {notification.remarks} 
                        </div>
                        <div className="date-style">{notification.notificationDate}</div>
                    </li>
                
              )

              )}
              </ul>
              </div>
                
                    
        </div>
    );
}

export default Notifications;