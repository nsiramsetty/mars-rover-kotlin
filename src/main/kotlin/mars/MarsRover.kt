package main.kotlin.mars

import main.kotlin.mars.utils.*
import java.io.File

class Grid(xCoordinate: Int, yCoordinate: Int){
    var xRight = xCoordinate
    var yRight = yCoordinate
    private var isValidGrid = true
    init {
        if(isInvalidCoordinate(xCoordinate) || isInvalidCoordinate(yCoordinate)) {
            isValidGrid = false
        }
        if(isValidGrid){
            marsTiles = arrayOf((0..xCoordinate).toList().toTypedArray(), (0..yCoordinate).toList().toTypedArray())
            xRight = xCoordinate
            yRight = yCoordinate
        }
    }

    fun validateGrid() : Boolean{
        return isValidGrid;
    }

    fun isInvalidCoordinate(coordinate: Int) : Boolean{
        return coordinate < 0 || coordinate > xMax || coordinate > yMax;
    }

    private fun isInvalidDirection(direction : Char) : Boolean{
        return !listOfValidDirections.contains(direction);
    }

    fun isInvalidMovement(direction : Char) : Boolean{
        return !listOfValidMovements.contains(direction);
    }

    fun initCoordinates(xCoordinate : Int, yCoordinate : Int, direction : Char) : Boolean{
        if(isInvalidCoordinate(xCoordinate) || isInvalidCoordinate(yCoordinate) || isInvalidDirection(direction)) {
            return false
        }
        xCurrent = xCoordinate
        yCurrent = yCoordinate
        currentDirection = direction
        return true
    }
}

class MarsRover(g : Grid) {
    private val grid = g;
    fun evaluateNewPosition(movement: Char): Boolean {
        if (grid.isInvalidMovement(movement)) {
            return false;
        }
        when (movement) {
            'L' -> processLeftMovement()
            'R' -> processRightMovement()
            'M' -> return processMovement()
        }
        return true;
    }

    private fun processMovement(): Boolean {
        when (currentDirection) {
            'N' -> {
                if (grid.isInvalidCoordinate(yCurrent + 1)) return false else yCurrent += 1
            }
            'S' -> {
                if (grid.isInvalidCoordinate(yCurrent - 1)) return false else yCurrent -= 1
            }
            'W' -> {
                if (grid.isInvalidCoordinate(xCurrent - 1)) return false else xCurrent -= 1
            }
            'E' -> {
                if (grid.isInvalidCoordinate(xCurrent + 1)) return false else xCurrent += 1
            }
        }
        return true
    }

    private fun processLeftMovement(){
        when(currentDirection){
            'N' -> currentDirection =
                'W'
            'S' -> currentDirection =
                'E'
            'E' -> currentDirection =
                'N'
            'W' -> currentDirection =
                'S'
        }
    }

    private fun processRightMovement(){
        when(currentDirection){
            'N' -> currentDirection =
                'E'
            'S' -> currentDirection =
                'W'
            'E' -> currentDirection =
                'S'
            'W' -> currentDirection =
                'N'
        }
    }
}

fun main(args: Array<String>) {
    val input = File(args[0]).readLines();
    val gridCords: List<String> = input[0].split(" ")
    val noOfLines = input.size - 1
    val grid = Grid(gridCords[0].toInt(), gridCords[1].toInt());
    if (!grid.validateGrid()) {
        println("Invalid Grid!")
        return
    }
    for(i in 1..noOfLines/2){
        val initCords: List<String> = input[2*i-1].split(" ")
        grid.initCoordinates(initCords[0].toInt(), initCords[1].toInt(), initCords[2].toCharArray()[0])
        val directions: String = input[2*i]
        val iterator = directions.toCharArray().iterator()
        val rover = MarsRover(grid)
        iterator.forEach lit@{
            if (!rover.evaluateNewPosition(it)) {
                println("Invalid Position!")
                return@lit
            }
        }
        println("$xCurrent $yCurrent $currentDirection")
    }
}
