import { bootstrapApplication } from '@angular/platform-browser';

import type { AppConfig } from './app/core/config/app-config.token';
import { APP_CONFIG } from './app/core/config/app-config.token';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

async function bootstrap(): Promise<void> {
  const configUrl = resolveConfigUrl();
  const response = await fetch(configUrl, { cache: 'no-store' });

  if (!response.ok) {
    throw new Error(`Unable to load runtime app config from ${configUrl}: ${response.status}`);
  }

  const runtimeConfig = (await response.json()) as AppConfig;

  await bootstrapApplication(AppComponent, {
    ...appConfig,
    providers: [...(appConfig.providers ?? []), { provide: APP_CONFIG, useValue: runtimeConfig }]
  });
}

bootstrap().catch((err) => console.error(err));

function resolveConfigUrl(): string {
  const hostname = window.location.hostname;
  const isLocalHost =
    hostname === 'localhost' ||
    hostname === '127.0.0.1' ||
    hostname === '::1';

  return isLocalHost ? '/app-config.json' : '/app-config.production.json';
}
