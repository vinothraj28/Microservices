import { bootstrapApplication } from '@angular/platform-browser';

import type { AppConfig } from './app/core/config/app-config.token';
import { APP_CONFIG } from './app/core/config/app-config.token';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

async function bootstrap(): Promise<void> {
  const response = await fetch('/app-config.json');

  if (!response.ok) {
    throw new Error(`Unable to load runtime app config: ${response.status}`);
  }

  const runtimeConfig = (await response.json()) as AppConfig;

  await bootstrapApplication(AppComponent, {
    ...appConfig,
    providers: [...(appConfig.providers ?? []), { provide: APP_CONFIG, useValue: runtimeConfig }]
  });
}

bootstrap().catch((err) => console.error(err));
