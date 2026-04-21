# Use cases

After reading this you'll know when a use-case earns its keep and when you're better off calling the repository directly.

## The pattern

One class per verb, with `operator fun invoke` so call sites read like function calls. Example — `CreateTodoUseCase.kt` earns its keep by adding validation:

```kotlin
class CreateTodoUseCase(private val repo: TodoRepository) {
    suspend operator fun invoke(title: String, description: String): Todo {
        if (title.isBlank()) throw AppException.Validation("title must not be blank")
        return repo.create(title.trim(), description.trim())
    }
}
```

## When to create one

- The call composes multiple repositories, use-cases, or includes non-trivial business rules (validation, derivation, ordering).
- You want a testable seam that doesn't require standing up the whole repository graph.
- You want a stable DI key that ViewModels depend on regardless of the underlying repo shape.

## When to skip it

- The use-case is a 1-line pass-through to the repository and adds no rule. `ToggleTodoUseCase`/`DeleteTodoUseCase` in this template are kept only for symmetry — feel free to inject the repo directly in your own features when the wrapper adds nothing.

Keeping the domain layer honest matters more than exhaustiveness.
