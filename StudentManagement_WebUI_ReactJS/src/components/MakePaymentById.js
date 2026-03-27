export const makePaymentById = async (id) => {

            const token = localStorage.getItem("token")
            const response = await fetch(`http://localhost:8081/students/payment/${id}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });


            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Failed to make payment: ${response.status} ${response.statusText} ${text}`);
            }

            if (response.status === 204) {
                return null;
            }

            const text = await response.text();
            if (!text) {
                return null;
            }

            let result;
            try {
                result = JSON.parse(text);
            } catch (err) {
                return text;
            }

            return result?.data ?? result;
  }