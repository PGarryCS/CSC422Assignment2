package csc422assignment2;

public class Pet
{
//attributes

    private String name;
    private int age;
//constructor

    Pet(String objectName, int objectAge) throws InvalidAgeException
    {
        if (objectAge < 0 || objectAge >= 20)
        {
            throw new InvalidAgeException("error: " + objectAge + " is not a valid age.");
        } else
        {
            name = objectName;
            age = objectAge;
        }
    }
//getters

    String getName()
    {
        return name;
    }

    int getAge()
    {
        return age;
    }
//setters

    void setName(String newName)
    {
        name = newName;
    }

    void setAge(int newAge)
    {
        age = newAge;
    }
}
