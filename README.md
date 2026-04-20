# Interval Training

Android-приложение для интервальных тренировок на Kotlin. Приложение загружает тренировку по ID, показывает интервалы, ведет таймер тренировки, поддерживает паузу, восстановление прогресса после сворачивания и звуковые сигналы для старта, переходов между интервалами и завершения тренировки.

## Технологии

- Kotlin
- Jetpack Compose
- Material 3
- Hilt
- Retrofit, OkHttp
- Kotlin Coroutines
- Gradle Kotlin DSL

## Требования

- Android Studio с поддержкой Android Gradle Plugin `9.1.1`
- JDK `17+`
- Android SDK:
  - `compileSdk` 36.1
  - `minSdk` 33
- Устройство или эмулятор с Android 13+

## Настройка проекта

Открой проект в Android Studio или используй Gradle wrapper из корня репозитория.

Файл `local.properties` должен находиться в корне проекта. Android Studio обычно создает в нем `sdk.dir` автоматически.

Пример:

```properties
sdk.dir=/path/to/android/sdk
baseUrl=https://example.com/
appToken=your_app_token
bearerToken=your_bearer_token
mockEnabled=true
```

Параметры:

- `baseUrl` - базовый URL API. Закрывающий `/` добавляется приложением автоматически, если он отсутствует.
- `appToken` - значение для заголовка `App-Token`.
- `bearerToken` - значение для заголовка `Authorization: Bearer ...`.
- `mockEnabled` - если `true`, приложение использует локальные mock-данные и не обращается к API.

Для локального запуска без backend можно оставить `mockEnabled=true`.

## Сборка

Собрать debug APK:

```bash
./gradlew assembleDebug
```

APK будет создан по пути:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Установить debug-сборку на подключенное устройство или эмулятор:

```bash
./gradlew installDebug
```

## Тесты

Запустить unit tests:

```bash
./gradlew test
```

Запустить instrumented tests на устройстве или эмуляторе:

```bash
./gradlew connectedDebugAndroidTest
```

## Основные сценарии приложения

- Ввод ID тренировки и загрузка программы интервалов.
- Старт тренировки с первого интервала.
- Автоматический переход к следующему интервалу после завершения текущего.
- Пауза и продолжение таймера.
- Сохранение состояния таймера при сворачивании приложения.
- Продолжение таймера в свернутом режиме до завершения тренировки.
- Отображение актуального прогресса после возврата в приложение.
- Звуковые сигналы:
  - 1 сигнал в начале тренировки;
  - 1 сигнал при переходе к следующему интервалу;
  - 2 сигнала при завершении тренировки.
