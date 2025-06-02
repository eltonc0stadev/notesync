# NoteSync

NoteSync é um aplicativo Android para criação, gerenciamento e marcação de notas, desenvolvido como trabalho acadêmico. **A reutilização, cópia ou distribuição deste projeto não é permitida, conforme as diretrizes acadêmicas do autor.**

## Tecnologias Utilizadas

### 1. **Kotlin**
O aplicativo é desenvolvido em Kotlin, a linguagem oficial para o desenvolvimento Android, proporcionando maior segurança, legibilidade e integração total com as APIs do Android.

### 2. **Android SDK**
Utiliza o Android SDK nível 35, garantindo compatibilidade com versões recentes do Android.

### 3. **AndroidX**
- `androidx.core:core-ktx`: Extensões para simplificar o uso de APIs do Android.
- `androidx.appcompat:appcompat`: Compatibilidade retroativa com versões antigas do Android.
- `androidx.activity`, `androidx.constraintlayout`: Gerenciamento de atividades e layouts responsivos.
- `androidx.test:runner`, `androidx.test.ext:junit`, `androidx.test.espresso`: Frameworks de teste para instrumentação e testes automatizados.

### 4. **Material Components**
- Utiliza a biblioteca Material (`com.google.android.material:material`) para adotar componentes visuais modernos seguindo as diretrizes da Google, como botões, diálogos e estilos.

### 5. **Gradle com Kotlin DSL**
- O sistema de build é configurado via arquivos `build.gradle.kts` (Kotlin DSL), proporcionando maior clareza e segurança de tipos na definição das dependências e plugins.

### 6. **Gerenciamento de Dependências**
- As dependências são centralizadas e gerenciadas via `libs.versions.toml` (não incluído aqui, mas referenciado), promovendo reutilização e atualização facilitada.

### 7. **Arquitetura e Componentes**
- **Activities**: Telas separadas para funcionalidades principais (criação/edição de nota, visualização de notas, perfil do usuário).
- **Intents e Resultados**: Comunicação entre telas para criação, edição e exclusão de notas.
- **Favoritos**: Sistema de marcação de notas como favoritas, utilizando tags no próprio arquivo da nota.
- **Persistência de Dados**: As notas são salvas em arquivos locais internos do app, utilizando APIs de I/O do Android (`File`, `openFileInput`, `openFileOutput`).
- **Interface Responsiva**: Uso de `GridLayout` para exibir notas de forma adaptável.

### 8. **Testes**
- **Unitários**: Implementados com JUnit.
- **Instrumentados**: Testes automatizados rodando em dispositivos/emuladores Android.

### 9. **Gerenciamento de Tema e Notificações**
- A interface inclui mudança de tema e ícones de notificação, promovendo uma experiência de usuário personalizada.

## Estrutura do Projeto

```
notesync/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   └── java/com/example/treino1/
│   │   │       ├── Telaprincipal.kt
│   │   │       ├── Telanota.kt
│   │   │       └── ...
│   ├── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

## Observação Importante

> **Este projeto foi desenvolvido exclusivamente para fins acadêmicos. É expressamente proibida a reutilização, publicação, distribuição ou qualquer uso fora do contexto original do trabalho.**

---

**Autor:** [eltonc0stadev](https://github.com/eltonc0stadev)
```
