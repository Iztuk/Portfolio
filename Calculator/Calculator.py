# This program functions as a calculator application that can add, subtract, multiply, divide, and sqrt.
import math

def main():
    quit = False
    while(not quit):
        print("--------------------------------")
        print("           Calculator")
        print("--------------------------------")
        print("1. Add")
        print("2. Subtract")
        print("3. Multiply")
        print("4. Divide")
        print("5. Square root")
        print("(Enter stop to exit the program)")
        print("--------------------------------")
        userInput = input("Enter the operation number: ")
        print("--------------------------------")

        # Conditional statements to direct the user to desired math operation.
        if(userInput == "1"):
            num1 = float(input("Enter the first addend: "))
            num2 = float(input("Enter the second addend: "))
            print("--------------------------------")
            result = add(num1, num2)
            print("The result is ", result)
        elif(userInput == "2"):
            num1 = float(input("Enter the minuend: "))
            num2 = float(input("Enter the subtrahend: "))
            print("--------------------------------")
            result = subtract(num1, num2)
            print("The result is ", result)
        elif(userInput == "3"):
            num1 = float(input("Enter the multiplicand: "))
            num2 = float(input("Enter the multiplier: "))
            print("--------------------------------")
            result = multiply(num1, num2)
            print("The result is ", result)
        elif(userInput == "4"):
            num1 = float(input("Enter the divedend: "))
            num2 = float(input("Enter the divisor: "))
            print("--------------------------------")
            try:
                result = divide(num1, num2)
                print("The result is ", result)
            except ZeroDivisionError:
                print("ERROR: Cannot divide by 0.")
        elif(userInput == "5"):
            num1 = float(input("Enter the radicand: "))
            print("--------------------------------")
            result = squareRoot(num1)
            print("The result is ", result)
        elif(userInput == "stop"):
            quit = True
            print("Good bye!")
            print("--------------------------------")
        else:
            print("ERROR: Enter a valid input.")
            print("--------------------------------")

        # Copy the result to the system clipboard.
        try:
            import tkinter as tk
            root = tk.Tk()
            root.withdraw()
            root.clipboard_clear()
            root.clipboard_append(result)
            root.update()
            print("Result has been copied to clipboard.")
            print("--------------------------------")
        except:
            print("Error: Failed to copy result to system clipboard.")

# Methods for the math operations
def add(num1, num2):
    return num1 + num2

def subtract(num1, num2):
    return num1 - num2

def multiply(num1, num2):
    return num1 * num2

def divide(num1, num2):
    return num1 / num2

def squareRoot(num1):
    return math.sqrt(num1)

main()
