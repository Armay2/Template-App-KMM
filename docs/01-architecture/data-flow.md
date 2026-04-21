# Data flow

After reading this you'll be able to trace any user intent from tap to state update.

## Diagram

```
  Screen (native)                 ViewModel (shared)             Domain (shared)            Data (shared)
  ---------------                 ------------------             ---------------            ---------------
  user taps row
      |
      | actions.onToggle(id) --->
      |                             toggle(id)  ---------->      ToggleTodoUseCase(id)
      |                                                              |
      |                                                              | repo.toggle(id)
      |                                                              v
      |                                                              TodoRepositoryImpl -- Ktor -->
      |                                                              (HttpClient.put /todos/{id})
      |                                                              |
      |                             update { ... new state ... }<----+
      |                             OR emit(SideEffect.ShowError)
      v
  state observed via
  StateFlow / AsyncSequence
  (re-render)
```

## Concrete example

From `TodoListViewModel.onToggle` (`shared/src/commonMain/kotlin/com/electra/template/presentation/todo/list/TodoListViewModel.kt`):

```kotlin
fun onToggle(id: String) {
    scope.launch {
        try {
            toggle(id)                          // ToggleTodoUseCase -> repo.toggle
        } catch (e: AppException) {
            emit(TodoListSideEffect.ShowError(e.message ?: "error"))
        }
    }
}
```

`TodoRepositoryImpl.toggle` (`shared/src/commonMain/kotlin/com/electra/template/data/todo/TodoRepositoryImpl.kt`) calls the API, mutates the in-memory `MutableStateFlow`, and returns. The list VM is already collecting `getTodos()` → `repo.todos` in `init {}`, so the UI re-renders automatically; no explicit state update is needed in the success path.

## Two outputs from a VM

Updates go into `StateFlow<State>` (UI re-renders). One-shot events — navigate, show snackbar, dismiss — go through `SharedFlow<SideEffect>` so late subscribers don't replay them. See `TodoListSideEffect` and `TodoDetailSideEffect` for concrete types and `BaseViewModel.kt` for the plumbing.
