package com.gram.landlord_client.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LandlordUtil {
    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static int getLeftRivalSeatNum(int yourSeatNum, int tableNum) {
        int max = tableNum * 3;
        if(max == yourSeatNum) return max - 1;
        if(max - yourSeatNum == 1) return max - 2;
        if(max - yourSeatNum == 2) return max;
        return -1;
    }

    public static int getRightRivalSeatNum(int yourSeatNum, int tableNum) {
        int max = tableNum * 3;
        if(max == yourSeatNum) return max - 2;
        if(max - yourSeatNum == 1) return max;
        if(max - yourSeatNum == 2) return max - 1;
        return -1;
    }

    /**
     * 给牌排序
     */
    public static void rankCards(ArrayList<Integer> unsortedCards) {
        Collections.sort(unsortedCards, ((o1, o2) -> {
            if(o1 % 20 > o2 % 20) return 1;
            else if(o1 % 20 < o2 % 20) return -1;
            return 0;
        }));
    }

    /**
     * 添加临时事件订阅
     */
    public static void addTemporaryDisposable(Disposable... disposables) {
        for(Disposable tmp : disposables) {
            compositeDisposable.add(tmp);
        }
    }

    public static void clearDisposables() {
        compositeDisposable.clear();
    }

    /**
     * 检查首次出牌合理性，只能出单张，对子，三代一，三代二，炸弹，五张以上的顺子，四代二，双王
     * 滚筒三代一和三代二，滚筒炸弹，滚筒四代二，6张以上的双对子
     */
    public static boolean isAvailable(List<Integer> cards) {
        if(cards.size() == 1) return true;
        if(cards.size() == 2) return getCouple(cards) != -1 || getTwoKing(cards);
        if(cards.size() == 3) return false;
        if(cards.size() == 4) return getThreeWithOne(cards) != -1 || getBomb(cards) != -1;
        if(cards.size() == 5) return getThreeWithTwo(cards) != -1 || getFullHouse(cards) != null || getFourWithOne(cards) != -1;
        if(cards.size() == 6) return getFullHouse(cards) != null || getFourWithTwo(cards) != -1 || getDoubleFullHouse(cards) != null;
        if(cards.size() > 6) return getFullHouse(cards) != null || getDoubleFullHouse(cards) != null
                || getMultiThreeWithOne(cards) != null || getMultiThreeWithTwo(cards) != null
                || getMultiFourWithOne(cards) != null || getMultiFourWithTwo(cards) != null;
        return false;
    }

    /**
     * 判断牌能不能接，以及自己出牌的合法性
     */
    public static boolean isCoverable(List<Integer> leftCards, List<Integer> myCards) {
        if(leftCards.size() == 1) return compareComplex1(leftCards, myCards);
        if(leftCards.size() == 2) return compareComplex2(leftCards, myCards);
        if(leftCards.size() == 4) return compareComplex4(leftCards, myCards);
        if(leftCards.size() == 5) return compareComplex5(leftCards, myCards);
        if(leftCards.size() == 6) return compareComplex6(leftCards, myCards);
        if(leftCards.size() > 6) return compareComplexMulti(leftCards, myCards);
        return false;
    }

    private static ArrayList<Integer> getRealCards(List<Integer> cards) {
        ArrayList<Integer> realCards = new ArrayList<>();
        for(int tmp : cards) {
            tmp = tmp % 20;
            realCards.add(tmp);
        }
        return realCards;
    }

    /**
     * 判断双王
     */
    public static boolean getTwoKing(List<Integer> cards) {
        if(cards.size() != 2) return false;
        return (cards.get(0) == 74 && cards.get(1) == 75) || (cards.get(0) == 75 && cards.get(1) == 74);
    }

    /**
     * 判断对子，不是对子就返回-1，是对子就返回牌面
     */
    private static int getCouple(List<Integer> cards) {
        if(cards.size() != 2 || cards.get(0) % 20 != cards.get(1) % 20) return -1;
        return cards.get(0) % 20;
    }

    /**
     * 判断三代一，是就返回牌面，不是就返回-1
     */
    private static int getThreeWithOne(List<Integer> cards) {
        ArrayList<Integer> copy = getRealCards(cards);
        int three = 0;
        for(Integer card : copy) {
            if(Collections.frequency(copy, card) == 3) three = card;
        }
        if(cards.size() != 4 || three == 0) return -1;
        return three;
    }

    /**
     * 判断串联三代一，是就返回牌面列表，不是就返回null
     */
    private static List<Integer> getMultiThreeWithOne(List<Integer> cards) {
        ArrayList<Integer> copy = getRealCards(cards);
        boolean isFullHouse = false;
        HashSet<Integer> set = new HashSet<>();
        for(Integer card : copy) {
            if(Collections.frequency(copy, card) == 3) set.add(card);
        }
        List<Integer> multiThree = new ArrayList<>(set);
        Collections.sort(multiThree);
        for(int i = multiThree.size() - 1; i > 0; i--) {
            if(multiThree.get(i) - multiThree.get(i-1) == 1) isFullHouse = true;
            else {
                isFullHouse = false;
                break;
            }
        }
        if(cards.size() % 4 != 0 || cards.size() <= 4 || cards.size() / 4 != multiThree.size() || !isFullHouse) return null;
        return multiThree;
    }

    /**
     * 判断炸弹，是炸弹就返回牌面，不是炸弹就返回-1
     */
    public static int getBomb(List<Integer> cards) {
        ArrayList<Integer> copy = getRealCards(cards);
        int bomb = 0;
        for(Integer card : copy) {
            if(Collections.frequency(copy, card) == 4) bomb = card;
        }
        if(cards.size() != 4 || bomb == 0) return -1;
        return bomb % 20;
    }

    /**
     * 串联炸弹，是就返回牌面，不是就返回null
     */
    private static List<Integer> getMultiBombs(List<Integer> cards) {
        ArrayList<Integer> copy = getRealCards(cards);
        HashSet<Integer> set = new HashSet<>();
        boolean isFullHouse = false;
        for(Integer card : copy) {
            if(Collections.frequency(copy, card) == 4) set.add(card);
        }
        List<Integer> bombs = new ArrayList<>(set);
        Collections.sort(bombs);
        for(int i = bombs.size() - 1; i > 0; i--) {
            if(bombs.get(i) - bombs.get(i-1) == 1) isFullHouse = true;
            else {
                isFullHouse = false;
                break;
            }
        }
        if(cards.size() % 4 != 0 || cards.size() <= 4 || cards.size() / 4 != bombs.size() || !isFullHouse) return null;
        return bombs;
    }

    /**
     *判断三代二，二必须是对子。是就返回牌面，不是就返回-1
     */
    private static int getThreeWithTwo(List<Integer> cards) {
        ArrayList<Integer> copy = getRealCards(cards);
        int three = 0;
        int two = 0;
        for(Integer card : copy) {
            if(Collections.frequency(copy, card) == 3) three = card;
            if(Collections.frequency(copy, card) == 2) two = card;
        }
        if(cards.size() != 5 || three == 0 || two == 0) return -1;
        return three;
    }

    /**
     *判断串联三代二，二必须是对子。是就返回牌面，不是就返回-1
     */
    private static List<Integer> getMultiThreeWithTwo(List<Integer> cards) {
        ArrayList<Integer> copy = getRealCards(cards);
        HashSet<Integer> set = new HashSet<>();
        boolean isFullHouse = false;
        boolean isAllCouple = false;
        for(Integer card : copy) {
            if(Collections.frequency(copy, card) == 3) set.add(card);
            if(Collections.frequency(copy, card) == 2) isAllCouple = true;
            if(Collections.frequency(copy, card) == 1 || Collections.frequency(copy, card) == 4) {
                isAllCouple = false;
                break;
            }
        }
        List<Integer> three = new ArrayList<>(set);
        Collections.sort(three);
        for(int i = three.size() - 1; i > 0; i--) {
            if(three.get(i) - three.get(i-1) == 1) isFullHouse = true;
            else {
                isFullHouse = false;
                break;
            }
        }
        if(cards.size() % 5 != 0 || cards.size() <= 5 || cards.size() / 5 != three.size() || !isFullHouse || !isAllCouple) return null;
        return three;
    }

    /**
     * 判断四代一，是就返回牌面，不是就返回-1
     */
    private static int getFourWithOne(List<Integer> cards) {
        ArrayList<Integer> copy = getRealCards(cards);
        int four = 0;
        for(Integer card : copy) {
            if(Collections.frequency(copy, card) == 4) four = card;
        }
        if(cards.size() != 5 || four == 0) return -1;
        return four % 20;
    }

    /**
     * 判断串联四代一，是就返回牌面，不是就返回-1
     */
    private static List<Integer> getMultiFourWithOne(List<Integer> cards) {
        ArrayList<Integer> copy = getRealCards(cards);
        HashSet<Integer> set = new HashSet<>();
        boolean isFullHouse = false;
        for(Integer card : copy) {
            if(Collections.frequency(copy, card) == 4) set.add(card);
        }
        List<Integer> four = new ArrayList<>(set);
        Collections.sort(four);
        for(int i = four.size() - 1; i > 0; i--) {
            if(four.get(i) - four.get(i-1) == 1) isFullHouse = true;
            else {
                isFullHouse = false;
                break;
            }
        }
        if(cards.size() % 5 != 0 || cards.size() <= 5 || cards.size() / 5 != four.size() || !isFullHouse) return null;
        return four;
    }

    /**
     * 判断四代二，是就返回牌面，不是就返回-1
     */
    private static int getFourWithTwo(List<Integer> cards) {
        ArrayList<Integer> copy = getRealCards(cards);
        int four = 0;
        for(Integer card : copy) {
            if(Collections.frequency(copy, card) == 4) four = card;
        }
        if(cards.size() != 6 || four == 0) return -1;
        return four % 20;
    }

    /**
     * 判断串联四代二，是就返回牌面，不是就返回-1
     */
    private static List<Integer> getMultiFourWithTwo(List<Integer> cards) {
        ArrayList<Integer> copy = getRealCards(cards);
        HashSet<Integer> set = new HashSet<>();
        boolean isFullHouse = false;
        for(Integer card : copy) {
            if(Collections.frequency(copy, card) == 4) set.add(card);
        }
        List<Integer> four = new ArrayList<>(set);
        Collections.sort(four);
        for(int i = four.size() - 1; i > 0; i--) {
            if(four.get(i) - four.get(i-1) == 1) isFullHouse = true;
            else {
                isFullHouse = false;
                break;
            }
        }
        if(cards.size() % 6 != 0 || cards.size() <= 6 || cards.size() / 6 != four.size() || !isFullHouse) return null;
        return four;
    }

    /**
     * 判断顺子，是就返回牌面列表，不是顺子就返回null，2和大小王不能当顺子
     */
    private static List<Integer> getFullHouse(List<Integer> cards) {
        boolean isFullHouse = false;
        List<Integer> sortedNum = new ArrayList<>();
        for(Integer card : cards) sortedNum.add(card % 20);
        Collections.sort(sortedNum);
        for(int i = sortedNum.size() - 1; i > 0; i--) {
            if(sortedNum.get(i) - sortedNum.get(i-1) == 1) isFullHouse = true;
            else {
                isFullHouse = false;
                break;
            }
        }
        if(cards.size() < 5 || !isFullHouse || sortedNum.get(sortedNum.size() - 1) >= 13) return null;
        return sortedNum;
    }

    /**
     * 双顺子，是就返回牌面列表，不是就返回null，同样2和大小王不能当顺子
     */
    private static List<Integer> getDoubleFullHouse(List<Integer> cards) {
        boolean isFullHouse = false;
        ArrayList<Integer> copy = getRealCards(cards);
        HashSet<Integer> set = new HashSet<>();
        for(Integer card : copy) {
            if(Collections.frequency(copy, card) == 2) set.add(card);
        }
        ArrayList<Integer> sortedNum = new ArrayList<>(set);
        Collections.sort(sortedNum);
        for(int i = sortedNum.size() - 1; i > 0; i--) {
            if(sortedNum.get(i) - sortedNum.get(i-1) == 1) isFullHouse = true;
            else {
                isFullHouse = false;
                break;
            }
        }
        if(cards.size() % 2 != 0 || cards.size() / 2 != sortedNum.size() || !isFullHouse
                || sortedNum.get(sortedNum.size() - 1) >= 13) return null;
        return sortedNum;
    }


    /**
     * 判断单张
     */
    private static boolean compareComplex1(List<Integer> leftCards, List<Integer> myCards) {
        if(myCards.size() == 1) {
            return leftCards.get(0) % 20 < myCards.get(0) % 20; //单张比大小
        } else if(myCards.size() == 2) {
            return getTwoKing(myCards); //双王
        } else if(myCards.size() == 4) {
            return getBomb(myCards) != -1; //炸弹
        }
        return false;
    }

    /**
     * 判断对子
     */
    private static boolean compareComplex2(List<Integer> leftCards, List<Integer> myCards) {
        if(myCards.size() == 2) {
            if(getTwoKing(leftCards)) return false; //双王要不起
            if(getTwoKing(myCards)) return true; //双王管所有
            return getCouple(leftCards) < getCouple(myCards); //对子比大小
        } else if(myCards.size() == 4) {
            return getBomb(myCards) != -1; //炸弹炸对子
        }
        return false;
    }

    /**
     * 判断三代一、炸弹
     */
    private static boolean compareComplex4(List<Integer> leftCards, List<Integer> myCards) {
        if(myCards.size() == 2) return getTwoKing(myCards); //双王管所有
        else if(myCards.size() == 4){
            if(getThreeWithOne(leftCards) != -1) { //三代一用三代一或者炸弹
                if(getThreeWithOne(myCards) != -1) return getThreeWithOne(leftCards) < getThreeWithOne(myCards);
                return getBomb(myCards) != -1;
            } else if(getBomb(leftCards) != -1) { //炸弹只能用炸弹
                return getBomb(leftCards) < getBomb(myCards);
            }
        }
        return false;
    }

    /**
     * 判断三代二、四代一、顺子
     */
    private static boolean compareComplex5(List<Integer> leftCards, List<Integer> myCards) {
        if(myCards.size() == 2) return getTwoKing(myCards); //双王管所有
        else if(myCards.size() == 4 && getFourWithOne(leftCards) == -1) return getBomb(myCards) != -1; //炸弹炸三代二、顺子
        else if(myCards.size() == 5) {
            if(getThreeWithTwo(leftCards) != -1 && getThreeWithTwo(myCards) != -1) return getThreeWithTwo(myCards) > getThreeWithTwo(leftCards); //三代二管三代二
            if(getFourWithOne(leftCards) != -1 && getFourWithOne(myCards) != -1) return getFourWithOne(myCards) > getFourWithOne(leftCards); //四代一对四代一
            List<Integer> left = getFullHouse(leftCards);
            List<Integer> me = getFullHouse(myCards);
            if(left != null && me != null) return left.get(0) < me.get(0);
        }
        return false;
    }

    /**
     * 判断四代二、顺子、双顺子
     */
    private static boolean compareComplex6(List<Integer> leftCards, List<Integer> myCards) {
        if(myCards.size() == 2) return getTwoKing(myCards); //双王管所有
        else if(myCards.size() == 4 && getFourWithTwo(leftCards) == -1) return getBomb(myCards) != -1; //炸弹炸顺子
        else if(myCards.size() == 6) {
            if(getFourWithTwo(leftCards) != -1 && getFourWithTwo(myCards) != -1) return getFourWithTwo(leftCards) < getFourWithTwo(myCards);
            List<Integer> left = getFullHouse(leftCards);
            List<Integer> me = getFullHouse(myCards);
            if(left != null && me != null) return left.get(0) < me.get(0);
            List<Integer> leftDouble = getDoubleFullHouse(leftCards);
            List<Integer> meDouble = getDoubleFullHouse(myCards);
            if(leftDouble != null && meDouble != null) return leftDouble.get(0) < meDouble.get(0);
        }
        return false;
    }

    /**
     * 判断顺子、双顺子、串联三代一、串联三代二、串联四代一、串联四代二
     */
    private static boolean compareComplexMulti(List<Integer> leftCards, List<Integer> myCards) {
        if(myCards.size() == 2) return getTwoKing(myCards); //双王管所有
        else if(myCards.size() == 4) { //炸弹炸顺子、串联三代一、串联三代二
            List<Integer> fullHouse = getFullHouse(leftCards);
            List<Integer> threeOne = getMultiThreeWithOne(leftCards);
            List<Integer> threeTwo = getMultiThreeWithTwo(leftCards);
            if(fullHouse != null || threeOne != null || threeTwo != null) return getBomb(myCards) != -1;
        } else if(myCards.size() > 6) {
            List<Integer> leftFullHouse = getFullHouse(leftCards);
            List<Integer> myFullHouse = getFullHouse(myCards);
            List<Integer> leftDoubleFullHouse = getDoubleFullHouse(leftCards);
            List<Integer> myDoubleFullHouse = getDoubleFullHouse(myCards);
            List<Integer> leftThreeOne = getMultiThreeWithOne(leftCards);
            List<Integer> myThreeOne = getMultiThreeWithOne(myCards);
            List<Integer> leftThreeTwo = getMultiThreeWithTwo(leftCards);
            List<Integer> myThreeTwo = getMultiThreeWithTwo(myCards);
            List<Integer> leftBombs = getMultiBombs(leftCards);
            List<Integer> myBombs = getMultiBombs(myCards);
            List<Integer> leftFourOne = getMultiFourWithOne(leftCards);
            List<Integer> myFourOne = getMultiFourWithOne(myCards);
            List<Integer> leftFourTwo = getMultiFourWithTwo(leftCards);
            List<Integer> myFourTwo = getMultiFourWithTwo(myCards);
            //顺子
            if(leftFullHouse != null && myFullHouse != null && leftFullHouse.size() == myFullHouse.size()) return leftFullHouse.get(0) < myFullHouse.get(0);
            //双顺子
            if(leftDoubleFullHouse != null && myDoubleFullHouse != null && leftDoubleFullHouse.size() == myDoubleFullHouse.size()) return leftDoubleFullHouse.get(0) < myDoubleFullHouse.get(0);
            //串联三代一
            if(leftThreeOne != null && myThreeOne != null && leftThreeOne.size() == myThreeOne.size()) return leftThreeOne.get(0) < myThreeOne.get(0);
            //串联三代二
            if(leftThreeTwo != null && myThreeTwo != null && leftThreeTwo.size() == myThreeTwo.size()) return leftThreeTwo.get(0) < myThreeTwo.get(0);
            //串联炸弹
            if(leftBombs != null && myBombs != null && leftBombs.size() == myBombs.size()) return leftBombs.get(0) < myBombs.get(0);
            //串联四代一
            if(leftFourOne != null && myFourOne != null && leftFourOne.size() == myFourOne.size()) return leftFourOne.get(0) < myFourOne.get(0);
            //串联四代二
            if(leftFourTwo != null && myFourTwo != null && leftFourTwo.size() == myFourTwo.size()) return leftFourTwo.get(0) < myFourTwo.get(0);
        }
        return false;
    }

    public static void main(String[] args) {
        ArrayList<Integer> left = new ArrayList<>(Arrays.asList(61, 41, 1, 21));
        ArrayList<Integer> me = new ArrayList<>(Arrays.asList(74, 75));
        System.out.println(getBomb(left));
        System.out.println(getTwoKing(me));
        System.out.println(isCoverable(left, me));
    }
}
