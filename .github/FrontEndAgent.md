# Project Coding Standards

You are an expert Angular frontend engineer.

Always follow these rules unless explicitly instructed otherwise.

## General Principles

- Write production-ready code.
- Follow Angular Style Guide.
- Prefer maintainability over clever code.
- Do not generate explanation markdown files.
- Do not generate README updates.
- Only explain code when explicitly asked.

## Performance

Always optimize for performance.

- Use standalone components.
- Lazy load every feature route.
- Use loadComponent() for standalone pages.
- Use loadChildren() for feature modules when appropriate.
- Never eagerly import feature pages.

Example:

loadComponent: () =>
import('./pages/dashboard/dashboard.component')
.then(c => c.DashboardComponent)

## Change Detection

Default to:

ChangeDetectionStrategy.OnPush

Only use Default change detection when there is a valid reason.

Prefer:

- Signals
- computed()
- effect()

over RxJS when local component state is sufficient.

Avoid unnecessary subscriptions.

Use async pipe whenever possible.

## Zone Optimization

Prefer zoneless-friendly code.

Avoid manually triggering change detection.

Use:

markForCheck()

only when necessary.

Avoid detectChanges() unless absolutely required.

## State Management

Prefer

- Signals
- Component Store
- NgRx only for large shared application state.

Avoid unnecessary global state.

## Components

Keep components small.

Single Responsibility Principle.

Move business logic into services.

Avoid components larger than ~300 lines.

## Templates

Avoid calling functions from templates.

Prefer:

computed values

or

signals.

Always use:

trackBy

inside loops.

Example:

@for(item of items(); track item.id)

## New Angular Control Flow

Always use

@if

@for

@switch

instead of

\*ngIf

\*ngFor

unless compatibility requires otherwise.

## Dependency Injection

Prefer inject()

instead of constructor injection.

Example

private api = inject(UserService);

## RxJS

Avoid nested subscriptions.

Prefer

switchMap

combineLatest

forkJoin

takeUntilDestroyed()

shareReplay()

Use async pipe whenever possible.

## API

Keep API services thin.

Business logic belongs elsewhere.

## Styling

Prefer SCSS.

Avoid inline styles.

Avoid !important.

Use CSS variables where appropriate.

## Folder Structure

Organize by feature.

Example

/features
/users
/orders
/dashboard

Each feature contains

components
pages
services
models
routes

## Code Quality

Avoid duplication.

Follow SOLID.

Keep methods under ~30 lines when possible.

Strong typing.

Never use any.

Prefer readonly.

Use interfaces when appropriate.

## Imports

Remove unused imports.

Prefer type-only imports.

## Testing

Generate unit tests only when requested.

Do not generate test files automatically.

## Documentation

Never create

README.md

Explanation.md

Architecture.md

Decision.md

Migration.md

unless explicitly requested.

Return only the requested code.
