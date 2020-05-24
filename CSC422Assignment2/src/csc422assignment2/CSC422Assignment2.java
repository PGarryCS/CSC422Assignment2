//Patrick Garry
//Initially Written for: Java CSC 222 - 11/11/19 - CSP
//Edited for: CSC 422 - 5/23/20
//
//PetDatabase - Assignment 2
//

//Updates:
//
// only support 5 entries (capacity)
// valid age is 0-20 (in pet.java)
// input accepts two values, name and age
// ID input by user must be an index of the array
//
// separate files
// array to arrayList
//

package csc422assignment2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Scanner;

public class CSC422Assignment2
{

//globals
    static int capacity = 5;
    static ArrayList<Pet> pets = new ArrayList<Pet>();
    //static Pet[] pets = new Pet[100];
    static Scanner s = new Scanner(System.in);
    static int petCount = 0;
    static File petsFile = new File("pets.txt");

    public static void main(String[] args) throws FullDatabaseException
    {
        loadDatabase();

        System.out.println("Pet Database Program.");

        Boolean running = true;
        while (running == true)
        {
            switch (getUserChoice())
            {
                case 1:
                    showAllPets();
                    break;
                case 2:
                    addPets();
                    break;
                case 3:
                    //InvalidIdException
                    try
                    {
                        removePet();
                    } catch (InvalidIdException e)
                    {
                        System.out.println("ID does not exist.");
                    }
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid Input. Reenter:\n");
                    break;
            }
        }
        saveDatabase();
    }

    private static void loadDatabase() throws FullDatabaseException
    {
//Scanner to read
        try
        {
            Scanner input = new Scanner(petsFile);
            String name;
            int age;

            while (input.hasNext())
            {
                name = input.next();
                age = input.nextInt();
                addPet(name, age);
            }
            System.out.println();
        } catch (IOException ex)
        {
            out.println("No such file.\nBuilding from scratch.\n");
        }
    }

    private static void saveDatabase()
    {
//PrintWriter to write to file
        try (PrintWriter writeToPets = new PrintWriter(petsFile);)
        {
            for (int i = 0; i < petCount; i++)
            {
                writeToPets.println(pets.get(i).getName() + " " + pets.get(i).getAge());
            }
            writeToPets.close();
        } catch (IOException ex)
        {
            out.printf("Something went wrong: %s\n", ex);
        }
    }

    private static int getUserChoice()
    {
        System.out.println();
//User options
        System.out.println("What would you like to do?\n");
        System.out.println("1) View all pets");
        System.out.println("2) Add new pets ");
        System.out.println("3) Remove an existing pet ");
        System.out.println("4) Exit program\n");
//User choice
        System.out.print("Your choice: ");
        int choice = s.nextInt();
        System.out.println();

        return choice;
    }

    private static void addPets()
    {
        Boolean running = true;
        s.nextLine();

        for (int i = 0; i < 100; i++)
        {
            while (running == true)
            {
                System.out.print("add pet (name, age): ");
                String userPetEntry = s.nextLine().trim();
                if ("done".equals(userPetEntry))
                {
                    running = false;
                    break;
                }
                parseArgument(userPetEntry);
            }
        }
    }

    private static void addPet(String name, int age) throws FullDatabaseException
    {
        if (petCount == capacity)
        {
            throw new FullDatabaseException("");
        } else
        {
//create new pet object, add into pets[] as object
//InvalidAgeException thrown when age is outside of range
            try
            {
                pets.add(new Pet(name, age));
            } catch (InvalidAgeException e1)
            {
                System.out.println(e1);
                petCount--;
            }
//add to running counter
            petCount++;
        }
    }

    private static String[] parseArgument(String line)
    {
//extract name
        String nameTemp = line.substring(0, line.indexOf(' '));
//extract age
        String ageTemp = line.substring(line.indexOf(' ') + 1);
//convert age String to Int
        int ageInt = Integer.parseInt(ageTemp);

//pass name and age to addPet()
        try
        {
            addPet(nameTemp, ageInt);
        } catch (FullDatabaseException e)
        {
            System.out.println("Error: Database is full.");
        }
        return null;
    }

    private static void showAllPets()
    {
        printTableHeader();
        for (int i = 0; i < petCount; i++)
        {
            printTableRow(i, pets.get(i).getName(), pets.get(i).getAge());
        }
        printTableFooter(petCount);
    }

    private static void removePet() throws InvalidIdException
    {
        showAllPets();
        System.out.println();
        System.out.print("Enter the pet ID to remove: ");
//user input
        int removeChoice = s.nextInt();

        if (removeChoice < 0 || removeChoice > petCount)
        {
            throw new InvalidIdException("");
        } else
        {
            System.out.println();
//print to user which pet was removed
            System.out.println(pets.get(removeChoice).getName() + " " + pets.get(removeChoice).getAge() + " is removed.");
//copy later items in array up one index
            for (; removeChoice < petCount - 1; removeChoice++)
            {
                pets.get(removeChoice).setName(pets.get(removeChoice + 1).getName());
                pets.get(removeChoice).setAge(pets.get(removeChoice + 1).getAge());
            }
            petCount--;
        }
    }

    private static void printTableHeader()
    {
        System.out.print("+---------------------+\n");
        System.out.print("| ID | NAME     | AGE |\n");
        System.out.print("+---------------------+\n");
    }

    private static void printTableRow(int id, String name, int age)
    {
        System.out.printf("| %2s | %-8s | %3d |\n", id, name, age);
    }

    private static void printTableFooter(int rowCount)
    {
        System.out.println("+---------------------+");
        System.out.println(rowCount + " rows in set.");
    }
}

//Excpetion classes
class InvalidAgeException extends Exception                                     //DONE
{
//construct an exception

    public InvalidAgeException(String s)
    {
        super(s);
    }
}

class InvalidArgumentException extends Exception                                //FIX
{
//construct an exception

    public InvalidArgumentException(String s)
    {
        super(s);
    }
}

class InvalidIdException extends Exception                                      //DONE
{
//construct an exception

    public InvalidIdException(String s)
    {
        super(s);
    }
}

class FullDatabaseException extends Exception                                   //DONE
{
//construct an exception

    public FullDatabaseException(String s)
    {
        super(s);
    }
}