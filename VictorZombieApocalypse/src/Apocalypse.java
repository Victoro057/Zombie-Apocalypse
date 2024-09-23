

public class Apocalypse {

    //instance data here.  What do you need to keep track of?
    //number of days left?

    //constructor.  How do we want to construct?
    //What will we initialize through the constructor?



    public enum Status{DAY, NIGHT}  // didn't use this
    public int numOfDaysSurvived;
    Status timeOfDay = null;    // didnt use this


    // If its day, the player can choose what the people will do
        /* Options: Scavenger to find random supplies
        *           Training gun skill
        *           Training blunt skill
        *
        *           // The people only have time to do 1 of these things 5 times a day
        *
        * */



    Utilities dayTime = new Utilities();

    public void apocalypseSimulator(){
        boolean survivedTheGame = false;

        int numOfDays = 1;
        System.out.println();
        System.out.println("You are obviously an 'A' student in NHCC pursuing a computer science degree. Your Object Oriented Programming class is just about to finish, as you hear strange sounds coming from outside the classroom.");
        System.out.println("Turns out your not the only one hearing those sounds, your whole class turns around and what you see absolutely astonishes you.");
        System.out.println("THAT'S A ZOMBIE! -screams the whole class.");
        System.out.println();
        System.out.println("Even though you can't believe your eyes, you remember... We live in 'MERICA! So you decide to test whether that zombie is real or not...");
        System.out.println("How? By testing it with your 2nd amendment!!!");
        System.out.println("You pull out your gun and shoot the zombie... Nice! He's gone.");
        System.out.println("You think... NOW I can save the whole school like from one of my dreams.");
        System.out.println("However, you aren't the only one taking advantage of the 2nd amendment, everyone in your classroom pulls out a gun. Professor Gorrill even has a shotgun!");
        System.out.println("Dreams won't become reality I guess...");
        System.out.println("You are lucky that only one zombie got inside the building this night, though.");
        System.out.println("There is many more of them coming...");

        for (int i = 0; i <= Utilities.START_NUM_DAYS; i++){    // makes this run for 10 days. If people didn't survive a night, it breaks out of the for loop and later if statements deal with displaying
            System.out.println();
            System.out.println("Day " + numOfDays + " begins");

            if (dayTime.day() == true){
                System.out.println("The daytime has passed.");
            }
            else {
                System.out.println("You have not survived the day, all people died from hunger.");
                survivedTheGame = false;
                break;
            }



            if (dayTime.night() == true){
                System.out.println("    Congratulations, you have survived this night!");
                numOfDaysSurvived++;
            }
            else {
                System.out.println("    You didn't survive this night. Womp Womp.");       // the night() method already displays the GAME OVER if the person didn't survive the night
                survivedTheGame = false;
                break;
            }



            numOfDays = numOfDays + 1;
        }
        if (numOfDays ==10){
            survivedTheGame = true;
        }
        if(survivedTheGame == true){
            System.out.println(" **********YOU WON**********");
            System.out.println("Congratulations, you have survived the zombie apocalypse at CBT");
            System.out.println("You may now continue being an 'A' student in Object Oriented Java");
            System.out.println(" **********GAME OVER*********");
        }
        else if (survivedTheGame == false){
            System.out.println(" **********YOU LOST**********");
            System.out.println("You survived " + numOfDaysSurvived + " nights.");
            System.out.println(" **********GAME OVER*********");
        }

        

    }














}
