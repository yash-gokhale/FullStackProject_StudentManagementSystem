import { useForm } from "react-hook-form";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { authLogin } from "./Login";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import "../css/Loginform.css";

export const LoginForm = ({ onSubmit }) => {
    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm();
    const queryClient = useQueryClient();

    const { mutate, data: loginData, isPending, error: loginError } = useMutation({
        mutationFn: (data) => authLogin(data),
        onSuccess: (data) => {
            queryClient.invalidateQueries({ queryKey: ["login"] });
            if (data?.status === 200) {
                const token = data.data.token;
                const role = data.data.role;
                const userId = data.data.studentId;
                localStorage.setItem("token", token);
                localStorage.setItem("role", role);
                localStorage.setItem("userId", userId);
                setTimeout(() => {
                    navigate("/");
                }, 2000);
            }
        }
    });

    const navigate = useNavigate();

    const submitHandler = async (data) => {
        try {
            mutate(data);
            if (onSubmit) {
                await onSubmit(data);
            } else {
                console.log("Login data: ", data);
            }
        } catch (err) {
            console.error("Login failed", err);
        }
    };

    return (
        <div className="container-style">
            <form onSubmit={handleSubmit(submitHandler)} className="form-style">
                <h2>Login Form</h2>

                <div className="field-style">
                    <label>Username/Email</label>
                    <input
                        type="email"
                        {...register("email", {
                            required: "Username/email is required",
                            minLength: {
                                value: 3,
                                message: "Minimum length is 3 characters"
                            }
                        })}
                    />
                    {errors.email && (
                        <p className="error-style">{errors.email.message}</p>
                    )}
                </div>

                <div className="field-style">
                    <label>Password</label>
                    <input
                        type="password"
                        {...register("password", {
                            required: "Password is required",
                            minLength: {
                                value: 6,
                                message: "Password must contain at least 6 characters"
                            }
                        })}
                    />
                    {errors.password && (
                        <p className="error-style">{errors.password.message}</p>
                    )}
                </div>


                <button
                    type="submit"
                    disabled={isSubmitting || isPending}
                    className="button-style"
                >
                    {isSubmitting || isPending ? "Loading..." : "Login"}
                </button>

                <div>
                    <br />
                    New user? <Link to="/auth/register">Register</Link>
                </div>
            </form>
                <br />
            <div>
                {loginData && <p>{loginData.message} for {loginData.data.role}</p>}
            {loginError && <p className="error-style">Login failed: {loginError.message}</p>}
            </div>

            
        </div>

    );
};