# ProteinTracker App

App Android nativo (Kotlin + Jetpack Compose) para controle de proteína diária com meta de 190g/dia.

## Funcionalidades

- **Home**: Progresso diário com anel de progresso, refeições do dia, ações rápidas
- **Logger**: Registrar refeições via receitas pré-cadastradas ou personalizadas
- **Receitas**: 50+ receitas do PDF "Chef Proteína" com filtros (doces/salgadas/rápidas/meal prep)
- **Progresso**: Visualização diária, semanal e mensal com estatísticas
- **Configurações**: Perfil (peso alvo/atual, proteína/kg), lembretes, notificações, exportar/importar CSV

## Tecnologias

- Kotlin + Jetpack Compose (Material3)
- Room Database + Hilt DI
- Coroutines + Flow
- MPAndroidChart (gráficos)
- Coil (imagens)
- kotlinx.serialization + kotlinx-datetime

## Requisitos

- Android Studio Koala (2024.1.1) ou superior
- JDK 17+
- Android SDK 34
- Dispositivo/Emulador Android 8.0+ (API 26)

## Build e Instalação

### Opção 1: Android Studio (Recomendado)
1. Abra o Android Studio
2. File > Open > Selecione a pasta `ProteinTrackerApp`
3. Aguarde sincronização do Gradle
4. Build > Build Bundle(s) / APK(s) > Build APK(s)
5. Instale o APK gerado em `app/build/outputs/apk/debug/app-debug.apk`

### Opção 2: Linha de comando (Linux/Mac)
```bash
cd ProteinTrackerApp
./gradlew assembleDebug
# APK em app/build/outputs/apk/debug/app-debug.apk
```

### Opção 3: Linha de comando (Windows)
```cmd
cd ProteinTrackerApp
gradlew.bat assembleDebug
```

## Estrutura do Projeto

```
app/src/main/java/com/proteintracker/
├── data/
│   ├── model/          # Models (Recipe, Meal, DayLog, UserSettings)
│   ├── dao/            # DAOs (RecipeDao, MealDao, UserSettingsDao)
│   ├── db/             # Room Database, Converters
│   └── repository/     # Repository pattern
├── di/                 # Hilt Modules
├── ui/
│   ├── home/           # HomeScreen
│   ├── logger/         # LoggerScreen
│   ├── recipes/        # RecipesScreen, RecipeDetailScreen
│   ├── progress/       # ProgressScreen (Daily/Weekly/Monthly)
│   ├── settings/       # SettingsScreen
│   └── common/         # Componentes reutilizáveis
├── viewmodel/          # ViewModels
├── util/               # RecipeDataLoader (50 receitas hardcoded)
└── MainActivity.kt     # Navigation + Bottom Bar
```

## Receitas Inclusas

50 receitas baseadas no PDF "Chef Proteína":
- **Dias 1-5**: 30 receitas principais (6 refeições/dia = ~190g proteína)
- **Bônus**: 10 receitas coringa para variar
- Categorias: Salgadas, Doces, Rápidas (<15min), Meal Prep
- Cada receita: ingredientes, instruções, macros, dicas do chef

## Personalização

### Alterar meta de proteína padrão
Em `UserSettings` (Models.kt):
```kotlin
val calculatedDailyTarget: Double = 190.0  // Altere aqui
```

### Adicionar receitas
Edite `RecipeDataLoader.kt` -> `getHardcodedRecipes()` ou crie `assets/recipes.json`

### Cores do tema
Edite `res/values/colors.xml` e `res/values/themes.xml`

## Licença

Projeto pessoal para controle nutricional.