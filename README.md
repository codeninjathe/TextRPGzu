# {GAME NAME TO BE DECIDED SOON^TM}

---

boss 
* hp, atk, def, difficulty (number of seconds per player turn), range (number of characters), crit multiplier (can reduce or increase damage dealt)

battle mechanic 
  * each turn has a difficulty.
  * a range of letters will appear (range of 5 could be a w d f z), you have difficulty number of seconds to remember it, then after that you have to remember it
  * the damage you deal is at first percentage based, number of correct charas vs total number of charas, and then a multiplier based on number you have in order. 
  * 0 correct: 0x multiplier
  * 1 correct: .5x multiplier
  * 2 correct: .75x multiplier
  * 3 correct: 1x multiplier
  * 4 correct: 1.25x multiplier
  * 5 correct: 1.5x multiplier
  * 6 correct: 1.625x multiplier
  * after this point the multiplier goes up by .125x for the number of correct 

boss turn:
  * a number is generated from 1 - crit multiplier (floor is zero) to 1 + crit multiplier (cap is 2)
  * then 3 characters appear, you have to get all 3 correct to have a 50% reduction in damage for that turn
  * then damage is dealt


level up/powers
  * as you level up, you gain certain abilities; the cooldown of the abilities persist throughout other battles
  * abilities: 
    *  level 5: +1 second during the battle, has a 5 turn cool down, +1 turn cool down for all other skills 
    *  level 10: 15 damage instantly, has a 7 turn cool down, +1 turn cool down for all other skills
    *  level 15: +5 seconds during the battle, has a 9 turn cool down, +2 turn cool down for all other skills
    *  level 20: -2 turn cool down for all other abilities, has a 10 turn cool down
    *  level 25: +10 second cool down, resets cooldown gauge for all of the other skills and adds 2 turns to all other skills
  * stat allocations:
    *  level 1 - 10: +4 all stats, 2 points allocated anywhere you choose
    *  level 11 - 20: +3 all stats, 2 points allocated anywhere you choose
    *  level 21 - 30: +2 all stats, 1 point allocated anywhere you choose
    *  level 31 - etc: +1 all stats, 1 point allocated anywhere you choose
    *  you start with a base of 10/1/1

sav file:

{player}.sav
```sh
name:{name}
level:{level}
allocations:{+hp}/{+atk}/{+def}
misc:{a string of 32 _, each of them representing a special parameter. if it's _ then nothing, else if it's a certain character then something's there}
```
