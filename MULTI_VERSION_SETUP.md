# Multi-Version Setup Guide

This project now supports multiple Minecraft versions using a multi-root workspace approach with Git branches.

## 🏗️ Project Structure

```
hunger-overhaul-reborn/
├── hunger-overhaul-reborn.code-workspace  # Cursor workspace file
├── 1.20.1/                                # Stable 1.20.1 version
│   ├── common/
│   ├── fabric/
│   ├── forge/
│   └── build.gradle
└── 1.21.1/                                # Development 1.21.1 version
    ├── common/
    ├── fabric/
    ├── forge/
    └── build.gradle
```

## 🌿 Git Branch Strategy

- **`main`**: Current development branch (1.20.1)
- **`1.20.1`**: Stable 1.20.1 branch with version folder
- **`1.21.1`**: Development 1.21.1 branch with version folder

## 🚀 Getting Started

### 1. Open Workspace in Cursor
```bash
# Open the workspace file in Cursor
cursor hunger-overhaul-reborn.code-workspace
```

### 2. Switch Between Versions
Use Cursor's Source Control panel to switch branches:
- **1.20.1**: For stable 1.20.1 development
- **1.21.1**: For 1.21.1 development and testing

### 3. Build and Run
Each version folder has its own Gradle setup:

```bash
# Build 1.20.1 version
cd 1.20.1
./gradlew build

# Build 1.21.1 version  
cd 1.21.1
./gradlew build

# Run Fabric client for 1.20.1
cd 1.20.1
./gradlew :fabric:runClient

# Run Fabric client for 1.21.1
cd 1.21.1
./gradlew :fabric:runClient
```

## 🔧 Cursor Workspace Features

### Pre-configured Tasks
The workspace includes pre-configured tasks accessible via `Ctrl+Shift+P` → "Tasks: Run Task":
- **Build 1.20.1**: Builds the stable version
- **Build 1.21.1**: Builds the development version
- **Run 1.20.1 Fabric Client**: Launches 1.20.1 Fabric client
- **Run 1.21.1 Fabric Client**: Launches 1.21.1 Fabric client

### Multi-Folder Context
- Cursor AI can see both versions simultaneously
- Easy comparison between versions
- Cross-version refactoring suggestions

### Git Integration
- **GitLens**: Visual branch history and blame
- **Source Control**: Easy branch switching
- **Git Graph**: See version evolution
- **Compare**: Side-by-side version differences

## 📋 Development Workflow

### Working on 1.20.1 (Stable)
1. Switch to `1.20.1` branch
2. Work in `1.20.1/` folder
3. Make changes and commit to `1.20.1` branch
4. Test and build in `1.20.1/` folder

### Working on 1.21.1 (Development)
1. Switch to `1.21.1` branch
2. Work in `1.21.1/` folder
3. Make changes and commit to `1.21.1` branch
4. Test and build in `1.21.1/` folder

### Merging Common Fixes
```bash
# Merge a fix from 1.20.1 to 1.21.1
git checkout 1.21.1
git merge 1.20.1

# Or cherry-pick specific commits
git cherry-pick <commit-hash>
```

## 🎯 Benefits

### ✅ Clean Version Separation
- No version conflicts
- Independent development streams
- Easy to maintain both versions

### ✅ Cursor AI Advantages
- Multi-folder context awareness
- Branch-aware suggestions
- Cross-version refactoring help

### ✅ Simple Workflow
- Easy branch switching
- Pre-configured build tasks
- Integrated Git management

### ✅ Easy Maintenance
- Merge common fixes between versions
- Independent testing and releases
- Clear version boundaries

## 🔄 Migration Notes

### From Single Version
- All existing code is preserved in `1.20.1/` folder
- `1.21.1/` folder contains updated dependencies
- Workspace file provides unified development experience

### Dependencies Updated for 1.21.1
- Minecraft: `1.20.1` → `1.21.1`
- Fabric API: `0.92.6+1.20.1` → `0.92.6+1.21.1`
- Forge: `1.20.1-47.4.1` → `1.21.1-47.4.1`
- AppleCore: `1.20.1-1.0.0` → `1.21.1-1.0.0`
- HarvestCraft: `1.20.1-1.0.0` → `1.21.1-1.0.0`

## 🆘 Troubleshooting

### Build Issues
- Ensure you're in the correct version folder
- Check that dependencies are available for the target version
- Verify Gradle wrapper is executable

### Git Issues
- Always commit changes before switching branches
- Use `git stash` for uncommitted changes
- Check branch status with `git status`

### Cursor Issues
- Reload workspace if folders don't appear
- Check that workspace file is properly configured
- Verify Git integration is working

## 📚 Additional Resources

- [Cursor Documentation](https://cursor.sh/docs)
- [VSCode Multi-root Workspaces](https://code.visualstudio.com/docs/editor/multi-root-workspaces)
- [Git Branching Strategies](https://git-scm.com/book/en/v2/Git-Branching-Branching-Workflows)
