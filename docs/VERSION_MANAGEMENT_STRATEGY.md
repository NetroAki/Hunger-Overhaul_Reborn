# Version Management Strategy for Hunger Overhaul Reborn

## Overview

This document outlines the strategy for managing multiple Minecraft versions (1.20.1 and 1.21.1) while maintaining feature parity and code quality.

## Current Setup

### Stonecutter Multi-Version System
- **Central Script**: `build.gradle.kts` - Shared build configuration
- **Common Code**: `common/` directory - Version-agnostic code
- **Version Overrides**: `1.20.1/`, `1.21.1/` directories - Version-specific overrides
- **Platform Variants**: Fabric and Forge for each version

## Recommended Workflow

### 1. Development Phases

#### Phase 1: 1.20.1 Development (Current)
- Focus all development on 1.20.1
- Test both Fabric and Forge extensively
- Build robust common code

#### Phase 2: 1.21.1 Migration
- Create version-specific overrides in `1.21.1/` directories
- Update APIs for 1.21.1 changes
- Test parity with 1.20.1 features

#### Phase 3: Dual Maintenance
- Maintain both versions simultaneously
- Apply bug fixes to both versions
- Add new features to both versions

### 2. Code Organization

#### Common Code (`common/`)
- 95%+ of the codebase should remain here
- Use `VersionDetector` for conditional logic
- Abstract version differences through interfaces

#### Version-Specific Code (`1.20.1/`, `1.21.1/`)
- Only override files that need version-specific changes
- Keep overrides minimal and focused
- Document why each override exists

### 3. Conditional Logic Patterns

#### Runtime Detection
```java
// Use VersionDetector for runtime checks
if (VersionDetector.is1_20_1()) {
    // 1.20.1 specific logic
} else if (VersionDetector.is1_21_1()) {
    // 1.21.1 specific logic
}
```

#### Platform Detection
```java
// Use CompatibilityLayer for platform abstraction
CompatibilityLayer.registerEvents(this);
```

#### Build-Time Conditionals
```kotlin
// In build.gradle.kts
if (minecraft == "1.20.1") {
    // 1.20.1 specific dependencies
} else if (minecraft == "1.21.1") {
    // 1.21.1 specific dependencies
}
```

### 4. Testing Strategy

#### Automated Testing
- Unit tests in `common/` (version-agnostic)
- Integration tests for each version
- Cross-platform compatibility tests

#### Manual Testing Checklist
- [ ] Feature works on 1.20.1 Fabric
- [ ] Feature works on 1.20.1 Forge
- [ ] Feature works on 1.21.1 Fabric (when available)
- [ ] Feature works on 1.21.1 Forge (when available)
- [ ] Configuration parity across versions
- [ ] Performance impact assessment

### 5. Parity Maintenance

#### Automated Checks
```bash
# Build script to verify both versions compile
./gradlew buildAllVersions

# Test script to run basic functionality tests
./gradlew testAllVersions
```

#### Manual Verification
- Compare feature lists between versions
- Cross-reference configuration options
- Verify API compatibility
- Check performance metrics

### 6. Release Management

#### Version Numbering
- Base version: `0.2.1`
- Platform suffix: `-fabric` or `-forge`
- MC version suffix: `+1.20.1` or `+1.21.1`
- Example: `hunger_overhaul_reborn-fabric-0.2.1+1.20.1.jar`

#### Release Process
1. Build all 4 variants (2 versions × 2 platforms)
2. Run automated tests on all variants
3. Manual testing of critical features
4. Create release archive with all JARs
5. Update version compatibility documentation

### 7. Branching Strategy

#### Main Branch (`main`)
- Always stable and releasable
- Contains latest features for all supported versions
- Requires all tests to pass

#### Development Branches
- `feature/xyz` - New features (target both versions)
- `fix/xyz` - Bug fixes (apply to both versions)
- `version/1.21.1-migration` - Version-specific work

#### Release Branches
- `release/v0.2.1-1.20.1` - Version-specific releases
- `release/v0.2.1-1.21.1` - Version-specific releases

## Implementation Plan

### Phase 1: Enable 1.21.1 Builds (Current)
- [x] Uncomment 1.21.1 versions in `settings.gradle.kts`
- [ ] Update build scripts for 1.21.1 dependencies
- [ ] Create basic 1.21.1 overrides
- [ ] Test compilation for all 4 variants

### Phase 2: API Migration
- [ ] Identify 1.21.1 API changes affecting the mod
- [ ] Create version-specific implementations
- [ ] Update CompatibilityLayer for version differences
- [ ] Test feature parity

### Phase 3: Automated Testing
- [ ] Set up CI/CD pipeline for all versions
- [ ] Create parity verification scripts
- [ ] Implement automated release process

## Risk Mitigation

### Version Drift Prevention
- Regular sync reviews between versions
- Shared documentation updates
- Cross-version code reviews

### Breaking Change Handling
- Version-specific feature flags
- Graceful degradation for unsupported features
- Clear documentation of version differences

### Performance Monitoring
- Benchmark both versions
- Monitor for performance regressions
- Optimize shared code paths

## Success Metrics

- ✅ All 4 variants build successfully
- ✅ 95%+ code sharing between versions
- ✅ Feature parity maintained across versions
- ✅ Automated testing covers all variants
- ✅ Release process takes < 30 minutes
- ✅ User reports < 5% version-specific bugs
