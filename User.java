package com.example.aleksei.meep;

public class User
{
    private String firstName;
    private String lastName;
    private String gender;
    private int age;
    private String email;
    private boolean profileComplete;


    public User()
    {

    }

    public User(String firstName, String lastName, String gender, int age, String email, boolean profileComplete)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.profileComplete = profileComplete;
    }

    public boolean getProfileComplete()
    {
        return profileComplete;
    }

    public void setProfileComplete(boolean b)
    {
        profileComplete = b;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

}
