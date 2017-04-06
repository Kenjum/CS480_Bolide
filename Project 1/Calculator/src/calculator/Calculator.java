/*
 *  Company: Bolide
 *  CEO: 
 *       Kenji Kiplinger
 *  Other Staff:
 *       Ka-Hei Lee, Luis Lopez, BGryce Metcalf, Ramon Moreno, Awais Ibrahim
 *
 *  The project had us design a calculator with standard functionality, 
 *  something similar to the standard calculators on computers. Along with the
 *  functionality, we had keybinds and and advanced functions which would be
 *  hidden and revealed when pressing the "advanced button."
 *
 *  Our submitted Preliminary development plan when going into this was "we're 
 *  good, believe use." We set up a something a little more concrete. We would
 *  design the layout in a gui, have a button that changes the screen size, 
 *  and then stomp out any bugs after our pretty basic funcitonality button 
 *  funcitons. 
 *
 *  For our design and architecture approach, we wanted something similar to 
 *  a standard calculator you would find on a computer. We laid out something
 *  similar in the swing editor. We then built upon the generated code. We gave
 *  had a few variables save the use inputs and when a certain function was 
 *  input, we would pass that to equal and check which appropriate thing it 
 *  should do.
 *
 *  As far as implementation, it was pretty straight forward. We had a few 
 *  variables for holding information to work with each other later. We had 
 *  buttons with immediate effects operate yield instand results. We would also 
 *  read from the display window and write to it. For the number buttons, we 
 *  would concatinate it to whatever was already in the text box. There was a
 *  lot of conversion between strings and doubles. 
 *
 *  The biggest issue we encountered was how to incorporate keybinds. We had 
 *  resources online, but we didn't quite understand how to interpret them.
 *  we eventually added to what some online resources suggest to do and it 
 *  worked, but this part definitely took the longest to figure out.
 *
 *  For Testing approach and testing data, we would test normal cases that would
 *  throw errors on a regular calculator like dividing by zero or something. We
 *  would also check things like what if some of our +/- buttons would work
 *  on the zero value. We mainly aimed for illegal arithmatic moves.
 *
 *  Somthing we would do for future improvements is have create more separate
 *  functions because we have some repeating code in our project and that's not
 *  necessary. Otherwise, everything else is pretty smooth.
 *
 */

package calculator;

//This is to create and work with an instance.
public class Calculator {

    static Interface sInterface = new Interface();
    
    public static void main(String[] args) {
        sInterface.setVisible(true);
        //immediately setup the proper size of the calculator.
        sInterface.setupSize();
    }
    
}
