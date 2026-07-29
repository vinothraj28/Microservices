# Auth Design System

This project now uses a shared auth page design layer defined in `src/styles/auth-design.css` and imported by `src/styles.css`.

## What to reuse

Use these classes when building a new branded page:

- `auth-page`: Full-screen branded background and base text color.
- `auth-page__orb auth-page__orb--left|right`: Decorative glow elements.
- `auth-layout`: Two-column page shell with showcase content and form card.
- `auth-layout--wide-card`: Wider card variant for larger forms such as registration.
- `auth-showcase`: Left-side marketing or instructional panel.
- `auth-showcase__eyebrow`: Small uppercase label above the title.
- `auth-showcase__title`: Large hero heading.
- `auth-showcase__lead`: Supporting paragraph.
- `auth-points`: Grid for key benefits or steps.
- `auth-point`: Reusable info tile.
- `auth-card`: Glass-style form container.
- `auth-card__header`: Header wrapper inside the card.
- `auth-chip`: Small status label for the card header.
- `auth-form`: Vertical form layout.
- `auth-form-grid`: Two-column layout for larger forms.
- `auth-field`: Wrapper for label, input, and validation text.
- `auth-field--full`: Span full width inside `auth-form-grid`.
- `auth-field__label`: Styled field label.
- `auth-input`: Shared input styling.
- `auth-input--code`: Centered, large MFA code input style.
- `auth-help`: Helper text below an input.
- `auth-validation`: Validation message style.
- `auth-button`: Primary call-to-action button.
- `auth-feedback auth-feedback--error`: Error banner.
- `auth-feedback auth-feedback--success`: Success banner.
- `auth-switch`: Inline secondary navigation row.
- `auth-link`: Branded action link.
- `auth-note`: Low-emphasis note at the bottom of a card.
- `auth-qr-panel`: Framed QR code section.
- `auth-qr-image`: QR code image styling.
- `auth-loading`: Loading state text.

## Recommended page structure

```html
<main class="auth-page">
  <div class="auth-page__orb auth-page__orb--left" aria-hidden="true"></div>
  <div class="auth-page__orb auth-page__orb--right" aria-hidden="true"></div>

  <section class="auth-layout">
    <aside class="auth-showcase">
      <p class="auth-showcase__eyebrow">Section label</p>
      <h1 class="auth-showcase__title">Hero message for the page.</h1>
      <p class="auth-showcase__lead">Optional supporting text that explains the step.</p>

      <div class="auth-points">
        <article class="auth-point">
          <strong>Point title</strong>
          <span>Short explanation.</span>
        </article>
      </div>
    </aside>

    <section class="auth-card">
      <header class="auth-card__header">
        <p class="auth-chip">Context label</p>
        <h2>Form title</h2>
        <p>Short description.</p>
      </header>

      <form class="auth-form">
        <label class="auth-field">
          <span class="auth-field__label">Field label</span>
          <input class="auth-input" type="text" placeholder="Value" />
          <small class="auth-help">Optional helper text.</small>
          <small class="auth-validation">Optional validation message.</small>
        </label>

        <button class="auth-button" type="submit">Continue</button>
      </form>

      <p class="auth-note">Optional note.</p>
    </section>
  </section>
</main>
```

## When to use component CSS

Keep component CSS minimal. Only add local styles when the page has a truly unique element that is not covered by the shared design system. For standard auth and form pages, prefer the shared classes instead of creating new component-specific variants.

## Current examples

These components already use the shared design system:

- `src/app/login/login.component.html`
- `src/app/register/register.component.html`
- `src/app/mfa-setup/mfa-setup.component.html`
- `src/app/verify-mfa/verify-mfa.component.html`
