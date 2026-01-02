package com.stickwar.legacy.systems;

import com.stickwar.legacy.entities.Unit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the upgrade system using Gems (for building upgrades) and Upgrade Points (for unit upgrades).
 * Gems are earned from victories and used to upgrade Barracks and Statue HP.
 * Upgrade Points are earned from victories and used to upgrade unit stats (HP, Attack, Speed).
 */
public class UpgradeManager {
    // Currency
    private int gems = 0;
    private int upgradePoints = 0;
    
    // Building upgrade levels (Gems)
    private int statueUpgradeLevel = 0;    // +200 HP per level
    private int barracksUpgradeLevel = 0;  // +100 HP per level
    
    // Unit upgrade levels (Upgrade Points) - per unit type
    private Map<String, Integer> unitHpLevels;     // +10% HP per level
    private Map<String, Integer> unitAttackLevels; // +10% Attack per level
    private Map<String, Integer> unitSpeedLevels;  // +5% Speed per level
    
    // Costs
    public static final int STATUE_UPGRADE_COST = 15;    // Gems
    public static final int BARRACKS_UPGRADE_COST = 10;  // Gems
    public static final int HP_UPGRADE_COST = 3;         // Upgrade Points
    public static final int ATTACK_UPGRADE_COST = 3;     // Upgrade Points
    public static final int SPEED_UPGRADE_COST = 2;      // Upgrade Points

    public UpgradeManager() {
        unitHpLevels = new HashMap<>();
        unitAttackLevels = new HashMap<>();
        unitSpeedLevels = new HashMap<>();
        for (String type : Arrays.asList("MINER", "SWORDWRATH", "ARCHIDONIS", "SPEARTON")) {
            unitHpLevels.put(type, 0);
            unitAttackLevels.put(type, 0);
            unitSpeedLevels.put(type, 0);
        }
    }
    
    // Currency management
    public void addGems(int amount) { 
        if (amount < 0) throw new IllegalArgumentException("Cannot add negative gems");
        gems += amount; 
    }
    
    public void addUpgradePoints(int amount) { 
        if (amount < 0) throw new IllegalArgumentException("Cannot add negative upgrade points");
        upgradePoints += amount; 
    }
    
    public int getGems() { return gems; }
    public int getUpgradePoints() { return upgradePoints; }
    
    // Building upgrades (Gems)
    public boolean upgradeStatue() {
        if (gems >= STATUE_UPGRADE_COST) {
            gems -= STATUE_UPGRADE_COST;
            statueUpgradeLevel++;
            return true;
        }
        return false;
    }
    
    public boolean upgradeBarracks() {
        if (gems >= BARRACKS_UPGRADE_COST) {
            gems -= BARRACKS_UPGRADE_COST;
            barracksUpgradeLevel++;
            return true;
        }
        return false;
    }
    
    public int getStatueBonusHp() { return statueUpgradeLevel * 200; }
    public int getBarracksBonusHp() { return barracksUpgradeLevel * 100; }
    public int getStatueUpgradeLevel() { return statueUpgradeLevel; }
    public int getBarracksUpgradeLevel() { return barracksUpgradeLevel; }
    
    // Unit upgrades (Upgrade Points)
    public boolean upgradeUnitHp(String unitType) {
        if (upgradePoints >= HP_UPGRADE_COST) {
            String type = unitType.toUpperCase();
            if (!unitHpLevels.containsKey(type)) {
                return false; // Unknown unit type
            }
            upgradePoints -= HP_UPGRADE_COST;
            unitHpLevels.put(type, unitHpLevels.get(type) + 1);
            return true;
        }
        return false;
    }
    
    public boolean upgradeUnitAttack(String unitType) {
        if (upgradePoints >= ATTACK_UPGRADE_COST) {
            String type = unitType.toUpperCase();
            if (!unitAttackLevels.containsKey(type)) {
                return false; // Unknown unit type
            }
            upgradePoints -= ATTACK_UPGRADE_COST;
            unitAttackLevels.put(type, unitAttackLevels.get(type) + 1);
            return true;
        }
        return false;
    }
    
    public boolean upgradeUnitSpeed(String unitType) {
        if (upgradePoints >= SPEED_UPGRADE_COST) {
            String type = unitType.toUpperCase();
            if (!unitSpeedLevels.containsKey(type)) {
                return false; // Unknown unit type
            }
            upgradePoints -= SPEED_UPGRADE_COST;
            unitSpeedLevels.put(type, unitSpeedLevels.get(type) + 1);
            return true;
        }
        return false;
    }
    
    // Apply upgrades to newly created units
    public void applyUnitUpgrades(Unit unit, String unitType) {
        String type = unitType.toUpperCase();
        int hpLevel = unitHpLevels.getOrDefault(type, 0);
        int attackLevel = unitAttackLevels.getOrDefault(type, 0);
        int speedLevel = unitSpeedLevels.getOrDefault(type, 0);
        
        // +10% HP per level
        int bonusHp = (int)(unit.getMaxHp() * 0.10 * hpLevel);
        unit.setMaxHp(unit.getMaxHp() + bonusHp);
        unit.setHp(unit.getMaxHp());
        
        // +10% Attack per level
        int bonusAttack = (int)(unit.getAttackPower() * 0.10 * attackLevel);
        unit.setAttackPower(unit.getAttackPower() + bonusAttack);
        
        // +5% Speed per level
        float bonusSpeed = unit.getMoveSpeed() * 0.05f * speedLevel;
        unit.setMoveSpeed(unit.getMoveSpeed() + bonusSpeed);
    }
    
    // Getters for upgrade levels
    public int getUnitHpLevel(String unitType) { 
        return unitHpLevels.getOrDefault(unitType.toUpperCase(), 0); 
    }
    
    public int getUnitAttackLevel(String unitType) { 
        return unitAttackLevels.getOrDefault(unitType.toUpperCase(), 0); 
    }
    
    public int getUnitSpeedLevel(String unitType) { 
        return unitSpeedLevels.getOrDefault(unitType.toUpperCase(), 0); 
    }

    /**
     * Resets all upgrades to initial state (for testing or new game).
     */
    public void reset() {
        gems = 0;
        upgradePoints = 0;
        statueUpgradeLevel = 0;
        barracksUpgradeLevel = 0;
        
        for (String type : Arrays.asList("MINER", "SWORDWRATH", "ARCHIDONIS", "SPEARTON")) {
            unitHpLevels.put(type, 0);
            unitAttackLevels.put(type, 0);
            unitSpeedLevels.put(type, 0);
        }
    }
}