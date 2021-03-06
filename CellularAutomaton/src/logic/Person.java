package logic;

import utils.Constants;
import utils.Sex;
import utils.Utils;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by roman on 10/2/16.
 */
public class Person {

    private int number;
    private Sex sex;
    private Color color;
    private boolean foundPartner;
    private Cell currentCell;
    private Cell nextCell;
    private int lastXDir = 0, lastYDir = 0;
    private boolean swapping = false;

    public Person(Sex sex, int number, Cell cell){
        this.sex = sex;
        this.number = number;
        currentCell = cell;
        nextCell = cell;
        color = Utils.calculateColorByNumber(number);
        foundPartner = false;
        currentCell.setOccupier(this);
    }

    public void setSwapping(){
        swapping = true;
    }

    public Sex getSex(){
        return sex;
    }

    public int getX(){
        return currentCell.getX();
    }

    public int getY(){
        return currentCell.getY();
    }

    public int getNumber() {
        return number;
    }

    public boolean isOpositeSex(Person other){
        return other.getSex() != this.getSex();
    }

    public Color getColor(){
        return color;
    }

    public void setNextCell(Cell cell){
        nextCell = cell;
        switch (sex) {
            case MALE:
                nextCell.setNextMaleOccupier(this);
                break;
            case FEMALE:
                nextCell.setNextFemaleOccupier(this);
                break;
        }
    }

    public Cell getNextCell(){
        return nextCell;

    }

    public int getLastXDir(){
        return lastXDir;
    }

    public int getLastYDir(){
        return lastYDir;
    }

    public void setCurrentCell(Cell cell) {
        this.currentCell = cell;
    }

    public Cell getCurrentCell(){
        return currentCell;
    }

    public void move(){
        if(nextCell != null) {

            lastXDir = nextCell.getX() - currentCell.getX();
            lastYDir = nextCell.getY() - currentCell.getY();

            if(!swapping) {
                if(Constants.LOG_PRINTS)
                    System.out.println("A " + sex + " at " + currentCell + " moved to " + nextCell);
                switch (sex) {
                    case MALE:
                        currentCell.setNextMaleOccupier(null);
                        nextCell.setNextMaleOccupier(null);
                        currentCell.setCurrentMaleOccupier(null);
                        nextCell.setCurrentMaleOccupier(this);
                        break;
                    case FEMALE:
                        currentCell.setNextFemaleOccupier(null);
                        nextCell.setNextFemaleOccupier(null);
                        currentCell.setCurrentFemaleOccupier(null);
                        nextCell.setCurrentFemaleOccupier(this);
                        break;
                }
            }
            else {
                if(Constants.LOG_PRINTS)
                    System.out.println("A swap occured");
                switch (sex) {
                    case MALE:
                        nextCell.setNextMaleOccupier(null);
                        nextCell.setCurrentMaleOccupier(this);
                        break;
                    case FEMALE:
                        nextCell.setNextFemaleOccupier(null);
                        nextCell.setCurrentFemaleOccupier(this);
                        break;
                }
                swapping = false;
            }
            currentCell = nextCell;
            nextCell = null;
        }
    }

    public boolean isSingle(){
        return getPartner() == null;
    }

    public Person getPartner(){
        if (sex == Sex.MALE)
            return currentCell.getCurrentFemaleOccupier();
        return currentCell.getCurrentMaleOccupier();
    }

    public int getMatchValue(){
        Person partner = getPartner();
        if(partner == null)
            return 0;
        return Constants.RANDOM_NUMBER_MAX - Math.abs(getNumber() - partner.getNumber());
    }

    public int getMatchValue(Person other){
        return Constants.RANDOM_NUMBER_MAX - Math.abs(getNumber() - other.getNumber());
    }

    public LinkedList<Cell> getNeighborCells(){
        return currentCell.getNeighbors();
    }
}
