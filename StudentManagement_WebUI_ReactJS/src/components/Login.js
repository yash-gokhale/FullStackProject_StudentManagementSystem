export const authLogin = async (data) => {
            const response = await fetch(`http://localhost:8081/auth/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            });


            if (!response.ok) {
                throw new Error("Failed to load student data!")
            }

            const result = await response.json();
            return result;
  }