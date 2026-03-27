import { useForm } from "react-hook-form";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { authRegister } from "./Register";
import { Link } from "react-router-dom";
import "../css/Loginform.css";

export const RegisterForm = ({ onSubmit }) => {
    const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm();
    const queryClient = useQueryClient();

    const { mutate, data: registerData, isPending, error: registerError } = useMutation({
        mutationFn: (data) => authRegister(data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["register"] });
        }
    });

    const submitHandler = async (data) => {
        try {
            mutate(data);
            if (onSubmit) {
                await onSubmit(data);
            } else {
                console.log("SignUp data: ", data);
            }
        } catch (err) {
            console.error("SignUp failed", err);
        }

            if(registerData && registerData.status == 201){
                reset();
            }
    };

    return (
        <div className="container-style">
            <form onSubmit={handleSubmit(submitHandler)} className="form-style">
                <h2>Register Form</h2>
                <div className="field-style">
                    <label>Username/Email</label>
                    <input
                        type="email"
                        {...register("username", {
                            required: "Username is required",
                            minLength: {
                                value: 3,
                                message: "Minimum length is 3 characters"
                            }
                        })}
                    />
                    {errors.username && (
                        <p className="error-style">{errors.username.message}</p>
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

                <div className="field-style">
                    <label>Re-Enter Password</label>
                    <input
                        type="password"
                        {...register("confirmPassword", {
                            required: "Please confirm your password",
                            validate: (value, formValues) => value === formValues.password || "Passwords do not match"
                        })}
                    />
                    {errors.confirmPassword && (
                        <p className="error-style">{errors.confirmPassword.message}</p>
                    )}
                </div>

                <div className="field-style">
                    <label>Role</label>
                    <select {...register("role")}>
                        <option value="ADMIN">ADMIN</option>
                        <option value="STUDENT">STUDENT</option>
                    </select>
                </div>

                <button
                    type="submit"
                    disabled={isSubmitting || isPending}
                    className="button-style"
                >
                    {isSubmitting || isPending ? "Loading..." : "Register"}
                </button>

                <div>
                    <br />
                    Existing user? <Link to="/auth/login">Login</Link>
                </div>
            </form>
                <br />
            <div>
                {registerData && <p>{registerData.message}</p>}
            {registerError && <p className="error-style">Registration failed: {registerError.message}</p>}
            </div>

            
        </div>

    );
};