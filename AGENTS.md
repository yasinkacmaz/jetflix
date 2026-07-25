# Agent Guide

## Purpose
Act as a senior Kotlin engineer collaborating on this Compose Multiplatform project.
Keep responses concise, clarify uncertainty before coding, and match the existing codebase style.
---

## Project Overview
Jetflix is a multiplatform movie streaming & exploration application built with Compose Multiplatform.
It displays movies, genres, filters, detailed cast & crew information, movie images, and allows managing favorites using an offline-first architecture.

### Core Concepts & Domain Features
**Movie Catalog & Filtering:** Movies are fetched from TMDB API with real-time filtering, genre filtering, and custom sorting options.
**Detail Views:** Deep screens for Movie Detail, Cast/Crew lists, Profile, and Image Galley viewers.
**Favorites & Preferences:** Local DataStore persistence for user favorites, dark mode state, and selected language.

## Code Style & Conventions

### Formatting & Naming
**Indentation:** 4 spaces. Max line length: 120 characters. Trailing commas allowed. End of line: LF.
**Files:** PascalCase matching primary class name. Platform: `{FileName}.{platform}.kt`. Tests: `{ClassName}Test.kt`.
**Composables:** PascalCase (`MoviesScreen`). Functions/Variables: camelCase. Constants: SCREAMING_SNAKE_CASE in companion objects. Private backing fields: Underscore prefix (`_uiState`).

### Package Organization
*   `data/`: `remote/`, `local/`, `repository/`, `model/`, `mapper/`.
*   `ui/`: Organized by `{feature}/` (e.g. `movies`, `moviedetail`, `favorites`, `profile`, `filter`) containing its own Screen, ViewModel, UiState, and mappers.

## Dependency Injection (Koin)
Use `singleOf(::ClassName)` for singletons and `viewModelOf(::ClassName)` for simple ViewModels.
Use `viewModel { (id: Int) -> ... }` factory lambda for parameterized ViewModels.
In Composables, use `koinViewModel()` or `koinInject()`. Differentiate parameterized viewmodels by passing a key: `koinViewModel(key = id.toString()) { parametersOf(id) }`.

## Architecture & Patterns
```
UI Layer (Compose) ←→ ViewModel ←→ Repository ←→ DataSource (Local/Remote)
```

### State & Logic Realization
**Simple Screens:** MVVM with `MutableStateFlow` and `.update { ... }`.
**Navigation:** Navigation 3 type-safe sealed class/interface routes with `@Serializable` implementing `NavKey`.
**Mappers:** Functional interfaces/mappers for explicit layer-to-layer data transformation.

## Compiler Options & Warnings Treatment
**Strict Warnings as Errors:** All Kotlin compilation tasks treat warnings as errors (`allWarningsAsErrors.set(true)`).
**Expect/Actual Support:** Expect/actual experimental class features are enabled via `-Xexpect-actual-classes` compiler options.

## Adaptive Layouts (Large Screen / Multi-Pane)

**Size Classing:** Use `rememberWindowSizeClass()`. Filter layouts using `windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact` or `calculatePaneScaffoldDirective(currentWindowAdaptiveInfoV2())` to isolate expanded screen branches.
**Scaffold Selection:** Use `ListDetailPaneScaffold` for canonical master-detail (Movies list → MovieDetail) navigation on large displays.
**Scaffold Invariants & Fixes:**
*   *The Value Overload:* Always use the `value` parameter overload (e.g., `value = scaffoldNavigator.scaffoldValue`), **not** `scaffoldState`. This prevents layout breaking when components leave/re-enter the tree.
*   *JetBrains KMP Resize Fix:* `rememberListDetailPaneScaffoldNavigator()` loses resize reactivity in KMP common. Always pass an explicit `scaffoldDirective` computed from `currentWindowAdaptiveInfoV2()`.
*   *Android Crash Hazard (Content Keys):* The adaptive navigator saves state via Android Bundle/Parcelable. **Never** use a custom route class (e.g., `Screen.MovieDetail`) as the content key type. Use `String` as the navigator key type, and save your true `@Serializable` routes in a separate `rememberSaveable(stateSaver = RouteSaver)`.
*   *Hiding/Blank Panes:* To fix a blank screen when resizing down to compact, check `scaffoldValue` in a `LaunchedEffect` and pop back to the List role if no item is selected.
**Drag Ranges & Dimensions:** Clamp drag ranges inside `consumeDragDelta` using an explicit width calculated via `Modifier.onSizeChanged` (imported from `androidx.compose.ui.layout.onSizeChanged`). Only initialize expansion state if `maxHorizontalPartitions > 1`.
**Imports:** Import `VerticalDragHandle` from `androidx.compose.material3` (material3 core), *never* from adaptive layouts.

## Compose UI Stability
**Compiler Reports:** The Compose compiler outputs diagnostic metrics inside `composeApp/build/compose_compiler/`. Force-regenerate these files by appending `clean` and the `--no-build-cache` flag (e.g., `./gradlew clean :composeApp:compileKotlinAndroidMain --no-build-cache`). Verify these reports after UI modifications to ensure no stability regressions (unstable parameters) are introduced.
**Stability Config:** Mark immutable external library classes (e.g. from `kotlinx-datetime`) as stable in the root `compose-stability-config.conf` configuration file.
**Explicit Stability:** Annotate custom read-only UI state holder classes and DTOs with `@Immutable` to ensure they are statically treated as stable by the Compose compiler.

## WASM Platform Pattern & Workarounds
When standard libraries (e.g., `androidx.datastore`) require special browser flows:
*   **DataStore Scope:** Always pass an explicit `CoroutineScope(Dispatchers.IO + SupervisorJob())` to `createDataStore` and use `WebLocalStorage(PreferencesSerializer, fileName)` on WASM to avoid flow deadlocks.
*   **Navigation Sync:** Use `com.github.terrakok:navigation3-browser` for syncing browser history with Navigation 3 state.

## Workflow & Constraints

### Git Commit Format
*   Simple descriptive messages in **imperative mood** without conventional commit prefixes (no `feat:`, `fix:`). Capitalized subject, no ending period, max 60 characters. Max body width 72 characters. Follow rules on https://cbea.ms/git-commit/.
*   *Examples:*
    *   `Implement list-detail layout on main screen`
    *   `Migrate navigation to navigation3`
    *   `Fix DataStore flow on Wasm`
    *   `Treat Kotlin warnings as errors`

### Operational Rules
*   Analyze structural design and existing codebase patterns before proposing changes. Match them exactly.
*   **Do not make assumptions.** If project criteria or requirements are ambiguous, halt execution and ask for clarity.
*   Surgically touch only relevant files. Keep existing changes in files you modify completely intact.
*   Execute `./gradlew ktlintFormat` prior to completion. Suggest a matching commit message when finished.

## Behavioral Core Guidelines

1.  **Think Before Coding:** Explicitly surface tradeoffs. Push back on over-engineering.
2.  **Simplicity First:** Write the minimum code required. No speculative flexibility.
3.  **Surgical Changes:** Clean up your own footprint, but do not touch, format, or refactor adjacent code that is outside the scope of the request.
4.  **Goal-Driven Execution:** Formulate explicit success criteria. Frame tasks into reproducible steps and loop until tests pass.
