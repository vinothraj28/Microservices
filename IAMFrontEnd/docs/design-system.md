# BookQuick Design System

> **Version:** 2.0  
> **Last Updated:** 2026-08-12  
> **Status:** Active

This document defines the design system architecture for the BookQuick application. All styles are organized in a layered architecture for maximum maintainability and consistency.

---

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Design Tokens](#design-tokens)
3. [Component Utilities](#component-utilities)
4. [Auth Pages](#auth-pages)
5. [Migration Guide](#migration-guide)
6. [Best Practices](#best-practices)

---

## Architecture Overview

### CSS Layer Structure (ITCSS-Inspired)

```
styles.css (entry point)
├── Layer 1: tokens.css        → Design tokens (CSS custom properties)
├── Layer 2: base.css           → Global resets and base elements
├── Layer 3: layout.css         → App shell, grid systems, containers
├── Layer 4: components.css     → Reusable UI patterns (buttons, cards, forms)
├── Layer 5: auth-design.css    → Authentication-specific layouts
└── Layer 6: Material overrides → Angular Material customizations
```

### File Organization

```
src/
├── styles/
│   ├── tokens.css          ← Unified design tokens (colors, spacing, etc.)
│   ├── base.css            ← Global resets and defaults
│   ├── layout.css          ← Layout utilities and app shell
│   ├── components.css      ← Reusable component patterns
│   ├── auth-design.css     ← Auth page styles
│   └── styles.css          ← Main entry point (imports all layers)
├── app/
│   └── */
│       └── *.component.css ← Component-specific styles ONLY
```

---

## Design Tokens

All design tokens are defined in `src/styles/tokens.css` and available globally.

### Color System

#### Background Colors

```css
--color-bg-primary: #07111f;
--color-bg-secondary: #0b1728;
--color-bg-tertiary: #0f1d2e;
--color-bg-hover: #14253a;
```

#### Auth/Marketing Backgrounds

```css
--color-bg-auth-start: #08131c;
--color-bg-auth-mid: #122838;
--color-bg-auth-end: #1d3747;
```

#### Surface Colors (cards, panels)

```css
--color-surface: rgba(10, 19, 28, 0.9);
--color-surface-soft: rgba(13, 27, 39, 0.84);
--color-surface-glass: rgba(7, 17, 31, 0.92);
```

#### Border Colors

```css
--color-border: rgba(148, 163, 184, 0.12);
--color-border-strong: rgba(148, 163, 184, 0.2);
--color-border-subtle: rgba(255, 255, 255, 0.08);
--color-border-auth: rgba(255, 255, 255, 0.14);
```

#### Text Colors

```css
--color-text: #f8fafc;
--color-text-secondary: #94a3b8;
--color-text-muted: #64748b;
--color-text-auth: #fffaf2;
--color-text-auth-soft: rgba(245, 239, 230, 0.74);
```

#### Primary & Accent Colors

```css
--color-primary: #38bdf8;
--color-primary-hover: #67d4ff;
--color-accent: #ff9f43;
--color-accent-strong: #ff7a18;
--color-accent-cool: #7ee0d6;
```

#### Semantic Colors

```css
--color-success: #34d399;
--color-warning: #fbbf24;
--color-danger: #fb7185;
--color-error: #dc2626;
```

### Spacing Scale

```css
--spacing-xs: 0.25rem; /* 4px */
--spacing-sm: 0.5rem; /* 8px */
--spacing-md: 1rem; /* 16px */
--spacing-lg: 1.5rem; /* 24px */
--spacing-xl: 2rem; /* 32px */
--spacing-2xl: 2.5rem; /* 40px */
--spacing-3xl: 3rem; /* 48px */
--spacing-4xl: 4rem; /* 64px */
```

### Typography

#### Font Families

```css
--font-primary: "Space Grotesk", "Segoe UI", "Inter", ui-sans-serif, sans-serif;
--font-system: ui-sans-serif, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
```

#### Font Sizes

```css
--font-size-xs: 0.75rem; /* 12px */
--font-size-sm: 0.875rem; /* 14px */
--font-size-base: 1rem; /* 16px */
--font-size-lg: 1.125rem; /* 18px */
--font-size-xl: 1.25rem; /* 20px */
--font-size-2xl: 1.5rem; /* 24px */
--font-size-3xl: 1.875rem; /* 30px */
--font-size-4xl: 2.25rem; /* 36px */
--font-size-5xl: 3rem; /* 48px */
```

#### Font Weights

```css
--font-weight-normal: 400;
--font-weight-medium: 500;
--font-weight-semibold: 600;
--font-weight-bold: 700;
--font-weight-extrabold: 750;
```

### Border Radius

```css
--radius-xs: 0.375rem; /* 6px */
--radius-sm: 0.5rem; /* 8px */
--radius-md: 0.75rem; /* 12px */
--radius-lg: 1rem; /* 16px */
--radius-xl: 1.25rem; /* 20px */
--radius-2xl: 1.5rem; /* 24px */
--radius-3xl: 1.75rem; /* 28px */
--radius-full: 9999px;
```

### Shadows

```css
--shadow-xs: 0 1px 2px rgba(0, 0, 0, 0.25);
--shadow-sm: 0 2px 4px rgba(0, 0, 0, 0.1);
--shadow-md: 0 12px 30px rgba(0, 0, 0, 0.25);
--shadow-lg: 0 20px 40px rgba(5, 13, 20, 0.18);
--shadow-xl: 0 28px 65px rgba(1, 8, 14, 0.45);
--shadow-auth: 0 28px 65px rgba(1, 8, 14, 0.45);
--shadow-button: 0 16px 28px rgba(255, 122, 24, 0.28);
--shadow-button-hover: 0 20px 34px rgba(255, 122, 24, 0.34);
--shadow-primary: 0 0 24px rgba(56, 189, 248, 0.18);
```

### Transitions

```css
--transition-fast: 150ms ease;
--transition-normal: 200ms ease;
--transition-slow: 300ms ease;
--transition-base: 180ms ease;
```

### Z-Index Scale

```css
--z-base: 1;
--z-dropdown: 100;
--z-sticky: 200;
--z-fixed: 300;
--z-modal: 500;
--z-popover: 600;
--z-tooltip: 700;
--z-notification: 1000;
--z-header: 1000;
--z-toast: 1100;
```

---

## Component Utilities

Reusable component patterns defined in `src/styles/components.css`.

### Buttons

```html
<!-- Primary button -->
<button class="btn btn-primary">Save Changes</button>

<!-- Secondary button -->
<button class="btn btn-secondary">Cancel</button>

<!-- Ghost button -->
<button class="btn btn-ghost">Learn More</button>

<!-- Danger button -->
<button class="btn btn-danger">Delete</button>

<!-- Size variants -->
<button class="btn btn-primary btn-sm">Small</button>
<button class="btn btn-primary btn-lg">Large</button>

<!-- Icon button -->
<button class="btn-icon">
  <svg>...</svg>
</button>
```

### Cards

```html
<!-- Standard card -->
<div class="card">
  <div class="card-header">
    <h2>Card Title</h2>
  </div>
  <div class="card-body">
    <p>Card content goes here.</p>
  </div>
  <div class="card-footer">
    <button class="btn btn-primary">Action</button>
  </div>
</div>

<!-- Glass card (auth pages) -->
<div class="card card-glass">...</div>

<!-- Elevated card -->
<div class="card card-elevated">...</div>
```

### Forms

```html
<form class="form">
  <!-- Single field -->
  <div class="form-field">
    <label class="form-label">Email</label>
    <input type="email" class="form-input" placeholder="you@example.com" />
    <small class="form-help">We'll never share your email.</small>
  </div>

  <!-- Two-column grid -->
  <div class="form-grid">
    <div class="form-field">
      <label class="form-label">First Name</label>
      <input type="text" class="form-input" />
    </div>
    <div class="form-field">
      <label class="form-label">Last Name</label>
      <input type="text" class="form-input" />
    </div>
  </div>

  <!-- Full-width field in grid -->
  <div class="form-field form-field--full">
    <label class="form-label">Bio</label>
    <textarea class="form-textarea"></textarea>
  </div>

  <button type="submit" class="btn btn-primary">Submit</button>
</form>
```

### Feedback Messages

```html
<!-- Error message -->
<div class="feedback feedback-error">Invalid email address. Please try again.</div>

<!-- Success message -->
<div class="feedback feedback-success">Account created successfully!</div>

<!-- Warning message -->
<div class="feedback feedback-warning">Your session will expire in 5 minutes.</div>
```

### Badges & Chips

```html
<!-- Badges -->
<span class="badge badge-primary">New</span>
<span class="badge badge-success">Active</span>
<span class="badge badge-danger">Urgent</span>

<!-- Chips -->
<span class="chip">Premium User</span>
```

### Links

```html
<a href="#" class="link">Learn More →</a>
```

---

## Auth Pages

Authentication-specific styles from `src/styles/auth-design.css`.

### Auth Page Structure

```html
<main class="auth-page">
  <!-- Decorative orbs -->
  <div class="auth-page__orb auth-page__orb--left" aria-hidden="true"></div>
  <div class="auth-page__orb auth-page__orb--right" aria-hidden="true"></div>

  <section class="auth-layout">
    <!-- Marketing showcase (left side) -->
    <aside class="auth-showcase">
      <p class="auth-showcase__eyebrow">Welcome Back</p>
      <h1 class="auth-showcase__title">Book tickets in seconds.</h1>
      <p class="auth-showcase__lead">Fast, secure, and simple ticket booking for all your favorite movies.</p>

      <div class="auth-points">
        <article class="auth-point">
          <strong>Instant Booking</strong>
          <span>Reserve seats in real-time</span>
        </article>
        <article class="auth-point">
          <strong>Secure Payment</strong>
          <span>Bank-grade encryption</span>
        </article>
        <article class="auth-point">
          <strong>Digital Tickets</strong>
          <span>No printing required</span>
        </article>
      </div>
    </aside>

    <!-- Form card (right side) -->
    <section class="auth-card">
      <header class="auth-card__header">
        <p class="auth-chip">Step 1 of 3</p>
        <h2>Sign In</h2>
        <p>Enter your credentials to continue.</p>
      </header>

      <form class="auth-form">
        <label class="auth-field">
          <span class="auth-field__label">Email</span>
          <input class="auth-input" type="email" placeholder="you@example.com" />
          <small class="auth-help">Use your registered email address.</small>
        </label>

        <label class="auth-field">
          <span class="auth-field__label">Password</span>
          <input class="auth-input" type="password" />
        </label>

        <button class="auth-button" type="submit">Continue</button>
      </form>

      <p class="auth-switch">
        Don't have an account?
        <a href="/register" class="auth-link">Sign up</a>
      </p>
    </section>
  </section>
</main>
```

### Wide Card Variant

For larger forms (e.g., registration), use the wide card modifier:

```html
<section class="auth-layout auth-layout--wide-card">...</section>
```

---

## Migration Guide

### Migrating from Old Token Names

| Old Token          | New Token                |
| ------------------ | ------------------------ |
| `--auth-bg-start`  | `--color-bg-auth-start`  |
| `--auth-text`      | `--color-text-auth`      |
| `--auth-text-soft` | `--color-text-auth-soft` |
| `--auth-cool`      | `--color-accent-cool`    |
| `--auth-accent`    | `--color-accent`         |
| `--auth-danger`    | `--color-danger-light`   |
| `--auth-danger-bg` | `--color-danger-bg`      |
| `--auth-success`   | `--color-success-light`  |
| `--auth-border`    | `--color-border-auth`    |
| `--auth-shadow`    | `--shadow-auth`          |
| `--color-bg`       | `--color-bg-primary`     |
| `--color-surface`  | `--color-bg-tertiary`    |

### Search & Replace Commands

Use these commands to migrate component files:

```bash
# Replace old auth tokens
var(--auth-cool) → var(--color-accent-cool)
var(--auth-accent) → var(--color-accent)
var(--auth-text) → var(--color-text-auth)
var(--auth-text-soft) → var(--color-text-auth-soft)

# Replace old color tokens
var(--color-bg) → var(--color-bg-primary)
var(--color-surface) → var(--color-bg-tertiary)
```

---

## Best Practices

### Component-Specific CSS

**DO:**

- Keep component CSS minimal
- Only add styles that are truly unique to the component
- Use design tokens for all color, spacing, and typography values
- Leverage utility classes from `components.css` and `layout.css`

**DON'T:**

- Define new design tokens in component files (use `:host` sparingly)
- Create duplicate button/card/form styles
- Use hardcoded colors, spacing, or font sizes
- Duplicate global layout patterns

### Example: Good Component CSS

```css
/* movie-details.component.css */
:host {
  display: block;
}

.movie-poster {
  position: relative;
  aspect-ratio: 2 / 3;
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.movie-rating-badge {
  position: absolute;
  top: var(--spacing-md);
  right: var(--spacing-md);
  background: var(--color-warning);
  color: #000;
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--radius-sm);
  font-weight: var(--font-weight-bold);
}
```

### Using Utility Classes

Prefer utility classes over creating custom styles:

```html
<!-- ✅ GOOD: Using utilities -->
<div class="grid grid-cols-3 gap-lg">
  <div class="card">...</div>
  <div class="card">...</div>
  <div class="card">...</div>
</div>

<!-- ❌ BAD: Custom CSS for common patterns -->
<div class="my-custom-grid">
  <div class="my-custom-card">...</div>
</div>
```

### Naming Conventions

- **BEM-style** for component-specific classes: `.component__element--modifier`
- **Utility-style** for reusable classes: `.flex`, `.gap-lg`, `.text-center`
- **Prefix auth-** for authentication page classes: `.auth-card`, `.auth-button`

### Accessibility

Always include:

- Proper focus states (handled by `base.css`)
- ARIA labels for decorative elements: `aria-hidden="true"`
- Screen reader only text: `<span class="sr-only">...</span>`
- Sufficient color contrast (verified by design tokens)

---

## Support & Questions

For questions about the design system, refer to:

- [Design Tokens Reference](../styles/tokens.css)
- [Component Examples](../styles/components.css)
- [Auth Pages Documentation](./auth-design-system.md) _(deprecated - use this document)_

**Last Updated:** 2026-08-12  
**Maintained By:** Frontend Team
