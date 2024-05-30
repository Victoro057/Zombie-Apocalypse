import java.util.Scanner;

public class Utilities {
    //constants to be used in place of magic numbers
    public static int START_NUM_DAYS = 10, START_NUM_FOOD = 80, START_NUM_AMMO = 150, START_NUM_PPL = 25;	// If we use private, we would have to use getters to access the values
    public int numDaysWithoutFood = 0;
    public int numDayPeopleCanLastWithoutFood = 2;
    public int numOfTasksAbleToDoInDay = 5;
    public int maxNumOfFoodFromScavenging = 50;
    public int maxNumOfAmmoFromScavenging = 80;
    public boolean backpacksUpgraded = false;
    public int numOfUpgradesMade;
    public static int GUN_HIT_PROB = 3;     // was changed from 5 to 3 to make the night more survivable    // upgrades when people train their gun skill
    public static int GUN_CRIT_PROB = 2;    // was changed from 3 to 2 to make the night more survivable   // upgrades when a person chooses to upgrade guns
    public int numOfTimesGunHitToKillZomb = 2;  // This number regulates the number of times a person has to hit a zombie with a bullet to kill it  // can be upgraded by getting better guns. After upgrades it, means that 1 bullet will kill a zombie
    public boolean gunsUpgraded = false;
    public static int GUN_MISS_KILLED_PROB = 2;
    public static int BLUNT_HIT_PROB = 3;   // not changed  // upgrades when people train their blunt skill
    public static int BLUNT_CRIT_PROB = 5; // was changed from 10 to 5 to make it more survivable   // upgrades when a person chooses to upgrade melee
    public boolean bluntUpgraded = false;
    public static int SURVIVOR_FOUND_PROB = 10;
    public static int MIN_NUM_OF_ZOMBIES_PER_NIGHT = 5;
    public static int MAX_NUM_OF_ZOMBIES_PER_NIGHT = 15;
    public static int NUM_OF_ZOMBIES_IN_CBT = 0;
    public static int GUN_SKILL_EXP = 0;
    public static int GUN_SKILL_LEVEL = 1;
    public static int BLUNT_SKILL_EXP = 0;
    public static int BLUNT_SKILL_LEVEL = 1;
    public int [] resources = new int[2];


                                                // ****** Main methods ******

    public boolean day(){
        System.out.println();
        System.out.println("The day has begun");
        System.out.println("You have " + People.food + " units of food left.");
        // Initializing;
        boolean dayPassed;
        int tasksDone = 0;
        int activityNumChosen;
        Scanner input = new Scanner(System.in);


        //Throughout the day, people can manage to do certain tasks numOfTasksAbleToDoInDay (currently set to 5) times
        for (int i = 0; i <= numOfTasksAbleToDoInDay - 1; i++) {
            System.out.println("You have time to either actions " + numOfTasksAbleToDoInDay + " times through the whole day. Choose wisely...");
            System.out.println("Number of actions done already: " + tasksDone);
            System.out.println("Your options are: ");
            System.out.println("1. Scavenge ");
            System.out.println("2. Train gun skill");
            System.out.println("3. Train blunt skill");
            System.out.println("\nEnter the number of the activity you want to do: ");
            activityNumChosen = input.nextInt();
            while (activityNumChosen < 1 || activityNumChosen > 3) {    // this makes sure that the person doesn't enter a invalid number of activities. Currently, the game has only 3 activities in the day
                // So this allows to enter 1 - 3 activities
                System.out.println("Invalid input! Please enter a number between 1 and 3 again.");
                activityNumChosen = input.nextInt();
            }
            if (activityNumChosen == 1) {
                scavenge();
                tasksDone = tasksDone + 1;
            } else if (activityNumChosen == 2) {
                trainGunSkill();
                tasksDone = tasksDone + 1;
            } else if (activityNumChosen == 3) {
                trainBluntSkill();
                tasksDone = tasksDone + 1;
            }
        }

        feedPeople();
        checkIfPplNotFedForTwoDays();

        if (People.NUM_OF_PEOPLE_ALIVE > 0) {
            dayPassed = true;
        }
        else{
            dayPassed = false;
        }

        return dayPassed;
    }


    public boolean night(){
        System.out.println("\n");   // should print 2 new lines
        System.out.println("The night has come, zombies will have to get inside ");


        boolean survivedTheNight = true;
        getRNoOfZombiesThatGotInside();
        System.out.println("The zombies got inside!");
        System.out.println("Now there are " + NUM_OF_ZOMBIES_IN_CBT + " of them!");
        System.out.println();
        while (NUM_OF_ZOMBIES_IN_CBT > 0) {
            if (checkIfAllZombiesDead() == true){       // This means that if all zombies are dead while the battle is still going, then the battle ends.
                break;                                      // Prevents the number of zombies to go into negatives
            }
            else{

            }
            if (battleLargeScale() > 0 && checkIfAllZombiesDead() == true) {
                survivedTheNight = true;
            }

            if (People.NUM_OF_PEOPLE_ALIVE <= 0) {  // If all people die in battle, and there is no people left alive returns false and breaks out of the while loop
                survivedTheNight = false;
                break;
            }
        }

        if(survivedTheNight == true) {  // executes only if the person survived the night
            checkAndDoUpgrades(); //  this method checks if the person can do any upgrades, and if they can, it makes a person choose an upgrade
        }
        return survivedTheNight;
    }





                                    // ************ Other methods **************//

            // Daily activities: Scavenge, Train gun skill, Train blunt skill,
            // Possible upgrades: Bigger backpacks for scavenging (allows to store more, if you do find more resources)
            //                    Better gun    (increases crit chance)
            //                    Better melee  (increases crit chance)
            // Notes: The statistics should be tabbed over when displayed. Makes the game look better

    // Upgrades     // This method is used in the night method because after fighting zombies and surviving, people are able to use EXP
    public void checkAndDoUpgrades() {
        boolean selectionMade = false;
        int upgradeNum;
        Scanner choice = new Scanner(System.in);
        if (numOfUpgradesMade < 3) {    // the below statements execute only if the person hasn't picked all 3 upgrades already.

            if (People.EXP >= 200) {    // This ensures that if a person reaches 200 or goes a little beyond it, to execute programs below
                System.out.println();
                System.out.println("You have reached enough EXP to make an upgrade!");
                System.out.println("Upgrades available: ");
                System.out.println("1. Bigger backpacks - allow you to store more when you go scavenging (maximum of resources able to get increases)");
                System.out.println("2. Better guns - allows you to make more critical shots (increases crit chance)");
                System.out.println("3. Better blunt weapons - allows you to make more critical hits (increases crit chance)");
                System.out.println("Please enter the number of the upgrade to make a selection: ");
                upgradeNum = choice.nextInt();
                while (upgradeNum <= 1 && upgradeNum >= 3) {  // This is input validation making sure the user only enters integer from 1 - 3. If the number of upgrades increases or decreases in the future, these numbers will need to be changed.
                    System.out.println("Invalid input! Please enter a number between 1 and 3 again.");  // This asks the user to enter the number of upgrade. If there will be more upgrades added, this will need to be changed
                    upgradeNum = choice.nextInt();
                }
                while (selectionMade == true) {
                    if (upgradeNum == 1 && backpacksUpgraded == false) {   // if 1 is chose (if bigger backpacks upgrade is chosen)
                        System.out.println("You chose to upgrade your backpacks.");
                        maxNumOfFoodFromScavenging += 30;   // increases the max amount of food able to get from scavenging by 30
                        maxNumOfAmmoFromScavenging += 20;   // increases the max amount of ammo able to get from scavenging by 20;
                        backpacksUpgraded = true;
                        numOfUpgradesMade++;    //adds 1 to the counter of upgrades made
                        selectionMade = true;
                    } else if (upgradeNum == 1 && backpacksUpgraded == true) {  // this executes when the person tries to upgrade backpacks, but has already done it before.
                        System.out.println("You have already upgraded your backpacks. Unfortunately you can't do it again.");
                    }

                    if (upgradeNum == 2 && gunsUpgraded == false) {
                        System.out.println("You chose to upgrade your guns.");
                        numOfTimesGunHitToKillZomb --;  // Decreases the value of number of times a gun has to hit to kill a zombie. Decreases it from 2 to 1, meaning it will kill every time it hits now
                        gunsUpgraded = true;
                        numOfUpgradesMade++; //adds 1 to the counter of upgrades made
                        selectionMade = true;
                    } else if (upgradeNum == 2 && gunsUpgraded == true) {  // This if statement executes if the person chooses to upgrade guns, but has already done that before.
                        System.out.println("You have already upgraded your guns. Unfortunately you can't do it again.");
                    }

                    if (upgradeNum == 3 && bluntUpgraded == false) {
                        System.out.println("You chose to upgrade your blunt weapons");
                        BLUNT_CRIT_PROB -= 2; // This decreases the probability number from 5 to 3 of getting a crit with a blunt weapon. Makes it more likely to hit a crit
                        bluntUpgraded = true;
                        numOfUpgradesMade++; //adds 1 to the counter of upgrades made
                        selectionMade = true;
                    } else if (upgradeNum == 3 && bluntUpgraded == true) {
                        System.out.println("You have already upgraded your blunt weapons. Unfortunately you can't do that again");
                    }

                }

                People.EXP -= 200;  // The person spent 200 EXP on a upgrade, so this removes 200 EXP from the person

            }

        }
    }




                 // Method that simulates scavenging
    public void scavenge(){
        boolean survivorFound = false;
            // Need to create some sort of random generator that generates random resources and their amounts
                // resources:   Food, ammo, possibility to find a survivor.

        resources[0] = generateRandomInRange(0,maxNumOfFoodFromScavenging); // probability food
        resources[1] = generateRandomInRange(0,maxNumOfAmmoFromScavenging); // probability ammo
        if (foundSurvivor() == true){   // This if statement creates a chance to find a survivor while scavenging
            People.NUM_OF_PEOPLE_ALIVE = People.NUM_OF_PEOPLE_ALIVE + 1;
            survivorFound = true;
        }

        People.food = People.food + resources[0];
        People.ammo  = People.ammo + resources[1];

                // Statistics
        System.out.println(); // to make a new line before printing the results. Makes it look better in game
        System.out.println("    You went scavenging.");
        System.out.println("    You found: \n* " + resources[0] + " units of food\n* " + resources[1] + " units of ammo");
        if (survivorFound == true){
            System.out.println("    * You found a survivor! Your numbers are growing!");
        }
        System.out.println("    Now you have " + People.food + " units of food");
        System.out.println("    Now you have " + People.ammo + " units of ammo");
    }
    public boolean foundSurvivor(){
        return generateRandomInRange(1,SURVIVOR_FOUND_PROB) == 1;    // this is the probability to find another survivor while scavenging.
    }


            // This method trains the gun skill of students, which will increase their chances of hitting the zombies with guns
    public void trainGunSkill(){
        int gunSkillLevel2 = 10;

            GUN_SKILL_EXP = GUN_SKILL_EXP + 1;  // Upgrading the gun skill of students

                // Statistics
        System.out.println();
        System.out.println("    Students have successfully trained gun skill!");
        System.out.println("    Students' current gun skill EXP is: " + GUN_SKILL_EXP);
        System.out.println("    Current gun skill level is: " + GUN_SKILL_LEVEL);

                // below 3 statements are talking about the next level of gun skill
        if (GUN_SKILL_EXP < 10){
            System.out.println("    The next level will be at 10 EXP");

        }
        else if (GUN_SKILL_EXP > 10){
            System.out.println("    You have reached the highest level of gun skill!");
        }


                // If certain level is reached, make the possibility of hitting the zombie higher by decreasing the probability

            if (GUN_SKILL_EXP == gunSkillLevel2){
                GUN_SKILL_LEVEL = GUN_SKILL_LEVEL + 1;
                GUN_HIT_PROB = GUN_HIT_PROB - 1;        // This makes the probability 1 out of 2
                System.out.println("    Congratulations, the students have upgraded their gun skill to level 2!");
                System.out.println("    They will now have a higher chance of hitting zombies with gun");
                System.out.println("    This is the highest level of gun skill!");
            }


        System.out.println();
    }

    public void trainBluntSkill(){
        int bluntSkillLevel2 = 10;
        int bluntSkillLevel3 = 20;

        BLUNT_SKILL_EXP = BLUNT_SKILL_EXP + 1;
        System.out.println();
        System.out.println("    Students have successfully trained blunt skill");
        System.out.println("    Students' current blunt skill EXP is: " + BLUNT_SKILL_EXP);
        System.out.println("    Current blunt skill level is: " + BLUNT_SKILL_LEVEL);

        // below 3 statements are talking about the next level of blunt skill
        if (BLUNT_SKILL_EXP < 10){
            System.out.println("    The next level will be at 10 EXP");

        }
        else if (BLUNT_SKILL_EXP < 20){
            System.out.println("    The next level will be at 20 EXP");
        }
        else if (BLUNT_SKILL_EXP > 20){
            System.out.println("    You have reached the highest level of gun skill!");
        }

        // If certain level is reached, make the possibility of hitting the zombie higher by decreasing the probability

        if (BLUNT_SKILL_EXP == bluntSkillLevel2){
            BLUNT_SKILL_LEVEL = BLUNT_SKILL_LEVEL + 1;
            BLUNT_HIT_PROB = BLUNT_HIT_PROB - 1;        // This makes the probability 1 out of 3
            System.out.println("    Congratulations, the students have upgraded their blunt skill to level 2!");
            System.out.println("    They will now have higher chance of shooting the zombies.");

        }
        else if (BLUNT_SKILL_EXP == bluntSkillLevel3){
            BLUNT_SKILL_LEVEL = BLUNT_SKILL_LEVEL + 1;
            BLUNT_HIT_PROB = BLUNT_HIT_PROB -1;     // This makes the probability 1 out of 2
            System.out.println("    Congratulations, the students have upgraded their blunt skill to level 3!");
            System.out.println("    They will now have higher chance of shooting the zombies.");
            System.out.println("    This is so far the highest level!");
        }
        System.out.println();
    }

    public void feedPeople(){   // this method feeds people
        int numPplFed = 0;
        int numPplNotFed = 0;
        // This method feeds people. When food runs out, stops feeding
            // each person requires 2 units of food a day
        for (int i = 0; i <= People.NUM_OF_PEOPLE_ALIVE - 1; i++) {
            if (People.food > 0) {
                People.food = People.food - 2;    // this makes it decrease food by 2 for every person alive
            } else if (People.food <= 0) {
                System.out.println("    You don't have enough food for everyone!!!");
                numPplNotFed = People.NUM_OF_PEOPLE_ALIVE - numPplFed;  // this gives us a number of people that weren't fed
                break;
            }
            numPplFed++;   // adds 1 to the number of people fed counter
        }

            // Statistics about people fed and not fed
        System.out.println("    Number of people fed today was: " + numPplFed);
        if(numPplFed == People.NUM_OF_PEOPLE_ALIVE){    // This is only when ALL people were fed
            System.out.println("    All people were fed!");
            People.NUM_OF_PEOPLE_SUFFERING_HUNGER = 0;
            numDaysWithoutFood = 0; // resets the counter of number of days without food
        }
        if (numPplNotFed > 0){  // This executes if not all people were fed
            System.out.println("    Number of people not fed today was: " + numPplNotFed);
            People.NUM_OF_PEOPLE_SUFFERING_HUNGER = People.NUM_OF_PEOPLE_SUFFERING_HUNGER + numPplNotFed;
            System.out.println("    Number of people suffering from hunger: " + People.NUM_OF_PEOPLE_SUFFERING_HUNGER);
            numDaysWithoutFood++;   // This makes it increase the counter for number of days without food.
        }
        System.out.println();   // just makes a empty line

    }

    public void checkIfPplNotFedForTwoDays(){   // This method checks if the people were not fed for two consecutive days.  // If a number of people are not fed on the 3rd day, they die.
        int numOfPeopleDeadFromHunger;  // This is a counter counting how many days a certain amount of people were without food
        if (numDaysWithoutFood >= numDayPeopleCanLastWithoutFood){  // This if statement executes if the number of people suffering hunger are equal to or less than people alive.
            if(People.NUM_OF_PEOPLE_SUFFERING_HUNGER <= People.NUM_OF_PEOPLE_ALIVE) {
                People.NUM_OF_PEOPLE_ALIVE = People.NUM_OF_PEOPLE_ALIVE - People.NUM_OF_PEOPLE_SUFFERING_HUNGER;
                numOfPeopleDeadFromHunger = People.NUM_OF_PEOPLE_SUFFERING_HUNGER;
                System.out.println("Unfortunately you are seeing people dying from hunger. \nToday " + numOfPeopleDeadFromHunger + " died from lack of food");
                System.out.println("You need to find food before even more people begin dying!!!");
                People.NUM_OF_PEOPLE_SUFFERING_HUNGER = 0;  // resetting the counter for people suffering hunger. Because they all died now
            }
            else if(People.NUM_OF_PEOPLE_SUFFERING_HUNGER > People.NUM_OF_PEOPLE_ALIVE){    // If the number of people suffering from hunger somehow ends up being more that people alive, this means that all people are starving
                People.NUM_OF_PEOPLE_SUFFERING_HUNGER = People.NUM_OF_PEOPLE_ALIVE;             // This could happen when there was a certain amount of people starving, and during the night people died. So on the next day there are less people but the number of people starving is the same from last day

                People.NUM_OF_PEOPLE_ALIVE = People.NUM_OF_PEOPLE_ALIVE - People.NUM_OF_PEOPLE_SUFFERING_HUNGER;
                numOfPeopleDeadFromHunger = People.NUM_OF_PEOPLE_SUFFERING_HUNGER;
                System.out.println("Unfortunately you are seeing people dying from hunger. \nToday " + numOfPeopleDeadFromHunger + " died from lack of food");
                System.out.println("You need to find food before even more people begin dying!!!");
                People.NUM_OF_PEOPLE_SUFFERING_HUNGER = 0;  // resetting the counter for people suffering hunger. Because they all died now
            }
        }

    }


    //  Method to calculate how many zombies get into CBT
    public int getRNoOfZombiesThatGotInside(){
        NUM_OF_ZOMBIES_IN_CBT = generateRandomInRange(MIN_NUM_OF_ZOMBIES_PER_NIGHT, MAX_NUM_OF_ZOMBIES_PER_NIGHT);      // Generates a random number of zombies between 10 and 50 that god inside CBT at night
        return NUM_OF_ZOMBIES_IN_CBT;   // returns the number of zombies that got inside
    }
    public boolean checkIfAllZombiesDead(){     // This method checks if all the zombies have been killed
                                                    // If not, the fight will have to be done again
        boolean allDead = false;

        if (NUM_OF_ZOMBIES_IN_CBT == 0){
            allDead = true;
        }
        else if (NUM_OF_ZOMBIES_IN_CBT >0){
            allDead = false;
        }

        return  allDead;
    }




                // This method below simulates a fight that people will have to face in the night
                    // This method does not account the ratio of the number of people to the number of zombies
                    // There will have to be some other method that accounts the case when all people that were sent died in battle, and there still are zombies left to kill
                        // Now its resolved by having the while loop in the night method. That while loop runs until there is 0 zombies in CBT
    public int battleLargeScale(){
        int expEarned = 0;
        int numPpl = 0;
        int numPplSurvived = 0;
        int numPplDied = 0;
        boolean survivedGunFight = false;
        boolean survivedBluntFight = false;

                // User's input and input validation
            System.out.println("You currently have " + People.NUM_OF_PEOPLE_ALIVE + " people alive. \nEnter the number of people to send out to fight: ");
            Scanner numOfPeopleToFight = new Scanner(System.in);
            numPpl = numOfPeopleToFight.nextInt();
            while (numPpl <= 0) {
                System.out.println("You have to send at least one person to fight!");
                numPpl = numOfPeopleToFight.nextInt();
            }
            while (numPpl > People.NUM_OF_PEOPLE_ALIVE) {
                System.out.println("There aren't even that many people left alive in CBT! Womp Womp");
                System.out.println("Number of people alive: " + People.NUM_OF_PEOPLE_ALIVE);
                System.out.println("Enter the number equal to or lower than the number of people alive");
                numPpl = numOfPeopleToFight.nextInt();
            }

            // This print statement is here to show how much ammo people have before hitting the battle
            System.out.println("At the begging your people had " + People.ammo + " units of ammo!");

            // This checks if a person has more than 0 ammo. If they do, they start a gunfight
            if (People.ammo > 0) {
                People.ranOutOfAmmo = false;   // If people have more than 0 ammo, that means they didn't run out of ammo, so false
                // Here struggled for over 40 minutes trying to figure out why is it running more than times of numPpl lol
                // because if you want it to run the exact amount of times, you have to do the number you want and put -1
                //like here


                    for (int i = 0; i <= numPpl -1; i++) {      // This runs the battle simulation as many times as the number of people sent to fight
                        // So these if else statements below run the gunFight and bluntFight methods and assign them if theyre true
                        // The reason I did it this was because before i was using gunFight() == true or false in almost every if statement. This was callig the gunFight method each time, and created a mess in the results
                        // When doing something like this, I have to call the methods once in the whole statement, otherwise it will be a mess
                        if (gunFight() == true) {
                            survivedGunFight = true;
                            if (checkIfAllZombiesDead() == true) {
                                break;  // break out of the for loop if all the zombies have been killed
                            }
                        } else {
                            survivedGunFight = false;
                        }

                        if (bluntFight() == true) {
                            survivedBluntFight = true;
                            if (checkIfAllZombiesDead() == true) {
                                break;  // break out of the for loop if all zombies have been killed
                            }
                        } else {
                            survivedBluntFight = false;
                        }


                        // The below if else checks if the people run out of ammo during the fight
                        if (survivedGunFight == true && People.ranOutOfAmmo == true) {
                            // The statements below simulates bluntFight in case, the person DID run out of ammo (as found if the if statement before) in the gunFight, so they have to switch to bluntFiht
                            // Simulates what happens when the person enters a blunt fight
                            if (survivedBluntFight == true) {
                                numPplSurvived = numPplSurvived + 1;
                                increaseEXPBy5();   // calls method to add 5 EXP
                                expEarned = expEarned + 5;
                            } else if (survivedBluntFight == false) {
                                numPplDied = numPplDied + 1;
                            }
                        }


                        // below statement says what happens if a person survives the gunfight WITHOUT running out of ammo
                        else if (survivedGunFight == true && People.ranOutOfAmmo == false) {
                            numPplSurvived = numPplSurvived + 1;
                            increaseEXPBy5();
                            expEarned = expEarned + 5;
                        } else if (survivedGunFight == false) {    //if person didn't survive the gunfight
                            numPplDied = numPplDied + 1;
                        }
                    }

            }



            // If people didn't have ammo initially
            else if (People.ammo == 0) {
                for (int i = 0; i < numPpl - 1; i++) {// if the people don't have ammo initially, they all have to fight with melee. This loop has each person fight with melee
                    System.out.println();
                    System.out.println("melee fight started");
                    System.out.println();
                    if (bluntFight() == true) {
                        numPplSurvived = numPplSurvived + 1;
                    } else {
                        numPplDied = numPplDied + 1;
                    }
                }
            }
            // Update the people alive count and the death count
            People.NUM_OF_PEOPLE_ALIVE = People.NUM_OF_PEOPLE_ALIVE - numPplDied;
            People.NUM_OF_PEOPLE_DEAD = People.NUM_OF_PEOPLE_DEAD + numPplDied;

            //Statistics / aftermath of the battle
            System.out.println("    * Number of people that fought and survived this battle: " + numPplSurvived);
            System.out.println("    * Number of people that fought and died in this battle: " + numPplDied);
            System.out.println("    * After the battle you have " + People.ammo + " units of ammo left");
            System.out.println("    * You earned " + expEarned + " EXP from this battle!");
            System.out.println("    * Your total EXP is: " + People.EXP);
            System.out.println("    * Number of zombies left: " + NUM_OF_ZOMBIES_IN_CBT);
            System.out.println("    * The number of people alive now: " + People.NUM_OF_PEOPLE_ALIVE);
        //return how much people survived
        return numPplSurvived;
    }


    public void increaseEXPBy5(){   // This method will be used to increase survival EXP for people. This bonus should apply only when a person wins a battle.
         People.EXP = People.EXP + 5;       // Note: the more people who fight and survive, the more exp they earn
    }
    // This method returns if a person has hit a zombie with a gun
    public boolean gunHit(){
        // uses generateRandomInRange to generate a number between 1 and 5. and if 1 is generated, then returns true (the player has hit the zombie with a gun)
        return generateRandomInRange(1, Utilities.GUN_HIT_PROB) == 1;
    }
            // This method below should only be used if the gun has hit a zombie
    public boolean gunCrit(){
        return generateRandomInRange(1, Utilities.GUN_CRIT_PROB) == 1;
    }

            // The method below is only used if the person has missed their shot with the gun.
                // If so, this method allows a 1/2 chance that the person will be killed by the zombie
    public boolean gunMissKilled(){
        return generateRandomInRange(1, Utilities.GUN_MISS_KILLED_PROB) == 1;
    }

    // This method returns if a person has hit a zombie with a blunt weapon or not
        // If the method returns false, then the person didn't land a hit with a blunt weapon, which means that zombie killed the player
    public boolean bluntHit(){
        return generateRandomInRange(1, Utilities.BLUNT_HIT_PROB) == 1;
    }
    public boolean bluntCrit(){
        return generateRandomInRange(1, Utilities.BLUNT_CRIT_PROB) == 1;
    }


            // Below is the gunFight simulation method. It takes the number of people who were sent (they must have ammo to start this fight), and does the fight for as many people as there are;
    public boolean gunFight(){
        int timesHit = 0;
        boolean crit = false;
        boolean survived = true;
        boolean killed = false;

                        for (int j = 0; j <= numOfTimesGunHitToKillZomb; j++) {      // The reason number 2 here is because the person has to hit the zombie 2 times (if they didn't land a crit) to kill it)


                            if (People.ammo <= 0) {  // this is done to opt out of gunfight since there is no more ammo, and in large scale battle will be directed to melee fight
                                People.ranOutOfAmmo = true; // This sets the value ranOutOfAmmo to true in the People class, indicating that people ran out of ammo
                                break;  // this should break out of the for loop
                            }


                            People.ammo = People.ammo - 1;


                            if (gunHit() == true) {
                                gunCrit();
                                if (gunCrit()) {
                                    crit = true;
                                    break;
                                }
                                else {
                                    timesHit = timesHit + 1;
                                }
                            }
                            else if (gunHit() == false) {
                                gunMissKilled();
                                if (gunMissKilled() == true) {
                                    killed = true;
                                    break;
                                }
                            }

                        }

                    // This is done to just stop the fight with guns, and in the large scale battle, the people will have to switch to melee/blunt fight
           if (People.ranOutOfAmmo == true){
               survived = true;
           }


           if(timesHit == 2){
               survived = true;
               NUM_OF_ZOMBIES_IN_CBT = NUM_OF_ZOMBIES_IN_CBT -1;
           }
           else if (crit == true){
               survived = true;
               NUM_OF_ZOMBIES_IN_CBT = NUM_OF_ZOMBIES_IN_CBT -1;
           }
           else if (killed == true){
               survived = false;
           }


            return survived;
    }

            // This method designs the blunt(melee) fight
    public boolean bluntFight(){
        boolean crit = false;
        boolean missed = false;
        boolean survived = true;
        int timesHit = 0;

                    // The for loop below runs 3 times if the person doesn't miss or doesn't crit
                        //if the person missed, they don't survive
                        //if the person critted, they instantly survive

                            // I put breaks in the loops so that if a person either crits or misses, there really is no need for loop to run again. its either they survived or died
            for (int i = 0; i <= 3; i++) {
                bluntHit(); // calls the method to find out if the person hit the zombie or not
                if (bluntHit() == true) {
                    bluntCrit();        // calls the method to find out if the person dealt a crit or not
                    if (bluntCrit() == true) {
                        crit = true;
                        break;
                    } else {
                        timesHit = timesHit + 1;
                    }
                } else {
                    if (bluntHit() == false) {
                        missed = true;
                        break;
                    }
                }
            }

                // All the if loops looking if a person survived
            if (crit == true) {
                survived = true;
                NUM_OF_ZOMBIES_IN_CBT = NUM_OF_ZOMBIES_IN_CBT -1;       // This means that if the person has made a crit, they have successfully
            }
            else if (timesHit == 3) {
                survived = true;
                NUM_OF_ZOMBIES_IN_CBT = NUM_OF_ZOMBIES_IN_CBT -1;       // This means that if the person has hit the zombie 3 times, the zombie is killed
            }
            else if (missed == true) {
                survived = false;
            }


        return survived;

    }


    //static methods that will be used in Apocalypse
    public static int generateRandomInRange(int min, int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
