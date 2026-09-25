import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {

    private static final File FILE =
            new File(System.getProperty("user.dir"),
                    "smart_study_users.dat");


    // ==========================================
    // LOAD ALL USERS
    // ==========================================

    @SuppressWarnings("unchecked")
    public static List<User> loadUsers() {

        System.out.println(
                "Data file location: "
                        + FILE.getAbsolutePath()
        );

        if (!FILE.exists()) {

            System.out.println(
                    "No data file found. Starting fresh."
            );

            return new ArrayList<>();
        }


        try (
                ObjectInputStream input =
                        new ObjectInputStream(
                                new FileInputStream(FILE)
                        )
        ) {

            Object object =
                    input.readObject();


            if (object instanceof List<?>) {

                List<User> users =
                        (List<User>) object;

                System.out.println(
                        "Users loaded successfully: "
                                + users.size()
                );

                return users;
            }


        } catch (InvalidClassException e) {

            System.out.println(
                    "Old data format detected."
            );

            System.out.println(
                    "Deleting incompatible data file..."
            );


            if (FILE.delete()) {

                System.out.println(
                        "Old data deleted successfully."
                );

            } else {

                System.out.println(
                        "Could not delete old data."
                );
            }


        } catch (Exception e) {

            System.out.println(
                    "Could not load users."
            );

            e.printStackTrace();
        }


        return new ArrayList<>();
    }


    // ==========================================
    // SAVE ALL USERS
    // ==========================================

    public static boolean saveUsers(
            List<User> users
    ) {

        System.out.println(
                "Saving data to:"
        );

        System.out.println(
                FILE.getAbsolutePath()
        );


        try (
                ObjectOutputStream output =
                        new ObjectOutputStream(
                                new FileOutputStream(FILE)
                        )
        ) {

            output.writeObject(users);
            output.flush();


            System.out.println(
                    "================================"
            );

            System.out.println(
                    "USER DATA SAVED SUCCESSFULLY"
            );

            System.out.println(
                    "Total users: "
                            + users.size()
            );

            System.out.println(
                    "File: "
                            + FILE.getAbsolutePath()
            );

            System.out.println(
                    "================================"
            );


            return true;


        } catch (IOException e) {

            System.out.println(
                    "================================"
            );

            System.out.println(
                    "FAILED TO SAVE USER DATA"
            );

            System.out.println(
                    "================================"
            );

            e.printStackTrace();

            return false;
        }
    }


    // ==========================================
    // FIND USER
    // ==========================================

    public static User findUser(
            List<User> users,
            String username
    ) {

        if (users == null) {
            return null;
        }


        for (
                User user
                : users
        ) {

            if (
                    user != null
                            &&
                            user.username != null
                            &&
                            user.username.equalsIgnoreCase(
                                    username
                            )
            ) {

                return user;
            }
        }


        return null;
    }


    // ==========================================
    // CHECK USERNAME
    // ==========================================

    public static boolean usernameExists(
            List<User> users,
            String username
    ) {

        return findUser(
                users,
                username
        ) != null;
    }


    // ==========================================
    // LOGIN
    // ==========================================

    public static User login(
            List<User> users,
            String username,
            String password
    ) {

        if (users == null) {
            return null;
        }


        for (
                User user
                : users
        ) {

            if (
                    user != null
                            &&
                            user.username != null
                            &&
                            user.password != null
                            &&
                            user.username.equalsIgnoreCase(
                                    username
                            )
                            &&
                            user.password.equals(
                                    password
                            )
            ) {

                return user;
            }
        }


        return null;
    }


    // ==========================================
    // SAVE / UPDATE ONE USER
    // ==========================================

    public static boolean saveUser(
            User user
    ) {

        if (user == null) {
            return false;
        }


        List<User> users =
                loadUsers();


        boolean found = false;


        for (
                int i = 0;
                i < users.size();
                i++
        ) {

            User existing =
                    users.get(i);


            if (
                    existing != null
                            &&
                            existing.username != null
                            &&
                            existing.username.equalsIgnoreCase(
                                    user.username
                            )
            ) {

                users.set(
                        i,
                        user
                );

                found = true;

                break;
            }
        }


        if (!found) {

            users.add(user);
        }


        return saveUsers(users);
    }
}