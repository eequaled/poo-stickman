# Stick War Legacy → Stickman Transformation Specification

## 🎯 Project Overview
Transform the existing Stick War Legacy Java game into a stickman-style game by integrating assets from the Java Stickman Game project while maintaining the core RTS gameplay mechanics.

## 📊 Current State Analysis

### Stick War Legacy (Current Game)
- **Framework**: libGDX 1.12.1
- **Language**: Java 11
- **Architecture**: Object-oriented with comprehensive testing
- **Game Type**: Real-time Strategy (RTS)
- **Resolution**: 1280×720 @ 60 FPS

### Current Unit System
1. **Miner** - Economic unit, generates gold
2. **Swordwrath** - Fast melee attacker (HP: 100, Attack: 20, Speed: 100, Cost: 200)
3. **Archidonis** - Ranged damage dealer
4. **Spearton** - Heavy tank (HP: 150, Attack: 20, Speed: 70, Cost: 500)

### Available Stickman Assets
- **Characters**: Character selection screens, stickman sprites
- **Backgrounds**: Multiple background images (BG2 Loop, various PNG files)
- **UI Elements**: Menu buttons, character selection UI
- **Animations**: Basic animation frames, grave sprite
- **Sounds**: Audio files for gameplay

## 🎮 Original Stick War Legacy Gameplay Analysis

### Core Mechanics
1. **Resource Management**: Gold-based economy starting with 200 gold
2. **Base Building**: Command Center (objective) + Barracks (unit production)
3. **Territory Control**: Dynamic frontline system with visual progress bar
4. **Combat System**: Range-based attacks with damage variance (+0-5)
5. **User Control**: Direct unit control grants 2x damage, 1.2x speed bonus

### Victory Conditions
- **Victory**: Destroy enemy Command Center
- **Defeat**: Your Command Center is destroyed
- **Rewards**: 10 Gems + 5 Upgrade Points per victory

### Strategic Elements
- **Economic vs Military Balance**: Miners vs combat units
- **Unit Composition**: Mixed armies (melee + ranged + tanks)
- **Positional Warfare**: High ground advantages, strategic placement
- **Timing**: Early rushes vs mid-game dominance

## 🔄 Transformation Plan

### Phase 1: Asset Integration
```
Priority: HIGH
Timeline: 1-2 weeks

Tasks:
1. Extract stickman sprites from Java Stickman Game
2. Create sprite sheets for each unit type:
   - Miner → Stickman with pickaxe/mining tool
   - Swordwrath → Stickman with sword
   - Archidonis → Stickman with bow
   - Spearton → Stickman with shield+spear
3. Integrate background images into game world
4. Replace UI elements with stickman-themed assets
5. Add stickman-style animations (walk, attack, idle)
```

### Phase 2: Visual Overhaul
```
Priority: HIGH
Timeline: 2-3 weeks

Tasks:
1. Update rendering system to use stickman sprites
2. Implement sprite animation system
3. Create visual effects for combat (sword swings, arrows)
4. Design stickman-style buildings (barracks, command center)
5. Update HUD with stickman-themed elements
```

### Phase 3: Gameplay Enhancements
```
Priority: MEDIUM
Timeline: 2-3 weeks

Tasks:
1. Add character selection system (from stickman game)
2. Implement weapon selection/upgrades
3. Add special abilities for stickman units
4. Create death animations (grave sprite already available)
5. Enhance particle effects and visual feedback
```

### Phase 4: Content Expansion
```
Priority: LOW
Timeline: 3-4 weeks

Tasks:
1. Add new unit types unique to stickman theme
2. Implement boss battles with giant stickmen
3. Create campaign mode with story elements
4. Add multiplayer support
5. Implement achievement system
```

## 🎨 Asset Mapping

### Stickman Game Assets → Stick War Legacy Units

| Original Asset | Target Unit | Required Modifications |
|---------------|-------------|------------------------|
| stickman.png | Base sprite template | Color variations per player |
| Character selection | Unit customization | Different weapons/armor |
| Sword sprites | Swordwrath weapons | Combat animations |
| Background images | Battle environments | Parallax scrolling |
| Menu buttons | UI overhaul | Stickman-themed design |
| Grave sprite | Death animations | Multiple variations |

### New Asset Requirements
```
- Mining animations (pickaxe swings)
- Bow drawing/release animations
- Shield blocking animations
- Building construction/destruction
- Territory control indicators
- Gold collection effects
```

## 🔧 Technical Implementation

### 1. Sprite System Enhancement
```java
// New class: StickmanSpriteRenderer
public class StickmanSpriteRenderer {
    private TextureAtlas stickmanAtlas;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> idleAnimation;
    
    public void renderUnit(Unit unit, SpriteBatch batch);
    public void updateAnimation(float deltaTime);
}
```

### 2. Asset Loading System
```java
// Enhanced asset manager
public class StickmanAssetManager {
    public void loadStickmanSprites();
    public void loadBackgrounds();
    public void loadUIElements();
    public void loadSounds();
}
```

### 3. Animation Controller
```java
// Unit animation states
public enum AnimationState {
    IDLE, WALKING, ATTACKING, MINING, DYING
}

public class UnitAnimationController {
    public void setAnimationState(AnimationState state);
    public void update(float deltaTime);
    public TextureRegion getCurrentFrame();
}
```

## 📋 Implementation Checklist

### Immediate Actions (Week 1)
- [ ] Extract and organize stickman assets
- [ ] Create sprite sheet structure
- [ ] Set up asset loading pipeline
- [ ] Test basic sprite rendering

### Core Features (Week 2-3)
- [ ] Implement unit sprite rendering
- [ ] Add basic animations (idle, walk)
- [ ] Replace placeholder graphics
- [ ] Update UI elements

### Advanced Features (Week 4-6)
- [ ] Combat animations
- [ ] Special effects
- [ ] Building graphics
- [ ] Environmental effects

### Polish Phase (Week 7-8)
- [ ] Performance optimization
- [ ] Sound integration
- [ ] Visual feedback systems
- [ ] Testing and bug fixes

## 🎯 Success Metrics

### Visual Goals
- [ ] All units rendered as stickmen
- [ ] Smooth animations (60 FPS maintained)
- [ ] Consistent art style throughout
- [ ] Clear visual feedback for actions

### Gameplay Goals
- [ ] Original mechanics preserved
- [ ] Enhanced visual appeal
- [ ] Intuitive stickman theme
- [ ] Improved player engagement

### Technical Goals
- [ ] No performance degradation
- [ ] Clean asset organization
- [ ] Maintainable code structure
- [ ] Comprehensive testing coverage

## 🚀 Deployment Strategy

### Testing Phases
1. **Asset Integration Testing** - Verify sprites load correctly
2. **Animation Testing** - Ensure smooth transitions
3. **Gameplay Testing** - Validate mechanics with new visuals
4. **Performance Testing** - Maintain 60 FPS target
5. **User Acceptance Testing** - Gather feedback on visual appeal

### Release Plan
- **Alpha Release**: Basic stickman sprites integrated
- **Beta Release**: Full animation system implemented
- **Final Release**: Polished stickman-themed game

## 📈 Risk Assessment

### High Risk
- **Asset Quality**: Stickman sprites may not match game requirements
- **Performance**: Animation system could impact frame rate
- **Art Consistency**: Maintaining cohesive visual style

### Medium Risk
- **Timeline**: Asset creation may take longer than expected
- **Integration**: Technical challenges with libGDX sprite system
- **User Acceptance**: Players may prefer original graphics

### Mitigation Strategies
- **Asset Pipeline**: Create automated sprite processing tools
- **Performance Monitoring**: Implement FPS tracking and optimization
- **Iterative Design**: Regular playtesting and feedback collection

## 🎮 Future Enhancements

### Post-Release Features
- **Custom Stickman Creator**: Allow players to design their own units
- **Weapon Shop**: Purchase and upgrade equipment
- **Skin System**: Unlockable visual variations
- **Emote System**: Stickman expressions and taunts
- **Level Editor**: Create custom battlefields

### Technical Improvements
- **Particle System**: Enhanced visual effects
- **Sound Design**: Stickman-themed audio
- **Mobile Port**: Adapt for touch controls
- **Online Multiplayer**: Networked battles

---

**Next Steps**: Begin Phase 1 asset extraction and integration while maintaining the existing game's core functionality and performance standards.
