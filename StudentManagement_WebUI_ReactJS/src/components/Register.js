export const authRegister = async (data) => {

            const token = localStorage.getItem("token");
            const response = await fetch(`http://localhost:8081/auth/register`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(data)
            });


            if (!response.ok) {
                throw new Error("Failed to load student data!")
            }

            const result = await response.json();
            return result;
  }