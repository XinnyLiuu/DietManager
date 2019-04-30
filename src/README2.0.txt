# TERM-PROJECT: Diet Manager

A diet manager application built with Java 8 and JavaFx using various software design patterns.

## Team

- Zach Easley
- Xin Liu
- Matt Marchinetti
- Amina Mahmood
- Samuel Ilesanmi

## Prerequisites

- Java 8
- IntelliJ

## Instructions (JAR)

1. Open a terminal of choice.
2. Navigate to the directory of `DietManager.jar` and ensure that the csv files are in the same directory (`food.csv`, `log.csv`, `exercise.csv`). The application requires these files to run properly !!
3. Enter the command `java -jar DietManager.jar`, this will execute both a CLI and GUI. Following the prompts in the CLI will update the GUI and vice versa.

## Instructions (Compiling)

1. Open an IDE of choice.
2. Find `DietManager.java` in the `/src` directory and compile/run the program. The program will start both the CLI and GUI. Following the prompts in the CLI will update the GUI and vice versa.
	- For users of IntelliJ: the csv files are fine being in either the `/src` directory or the root directory. This repository holds them in the root directory.
	- For users of other IDEs: if the program does not load information properly, or may be lacking some data, move the csv files from the root directory to the `/src` directory.

## Guide

The application currently supports:
1. Logging of weight, calorie, foods, recipes and exercises by date.
2. Removal of logged foods, recipes and exercises by date.
3. Viewing all logged foods, recipes and exercises by date.
4. Creation of foods, recipes and exercises.
5. Calculation of total nutritional intake (caloric intake as well).

## Bugs

Known bugs:
1. On a fresh day (without any logs for the specified day), the 'Total Nutrition Intake' graph will display bars even though the default is at 0%. This is not an issue on our team's end, but rather the JavaFx handling of multiple threads.  Multithreading is used in this application to ensure that the CLI and GUI are running in parallel.
2. The GUI might not be able to render properly on displays smaller than `1920 x 1080`. However, the GUI is resizable via dragging. Otherwise, all functionality is present in the CLI.

