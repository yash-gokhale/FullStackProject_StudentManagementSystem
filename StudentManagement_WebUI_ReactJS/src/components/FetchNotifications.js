export const fetchNotifications = async () => {

            const token = localStorage.getItem("token")
            const response = await fetch(`http://localhost:8083/notification`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });


            if (!response.ok) {
                throw new Error("Failed to load notifications!")
            }

            const result = await response.json();
            return result.data;
  }