# CLAUDE.md - Compiler_Java Project Guide

## Project Overview

This is a Java-based compiler implementation that performs lexical and syntactic analysis on a custom programming language with Spanish-inspired keywords. The compiler tokenizes source code, builds symbol tables, detects errors, and validates syntax.

### Language Characteristics

The target language supports:
- Variable declarations (VAR) with types (ENTERA, REAL)
- Constants (CONST) and labels (ETIQ)
- Control structures: SI/ENTONCES/SINO/FIN_SI, MIENTRAS/HAZ/FINM, REPITE/HASTA
- Logical operators: OR, AND, NOT
- Arithmetic and relational operators
- I/O operations: LEE, ESCRIBE, IMPRIME
- Jump statements: SALTA

## Repository Structure

```
Compiler_Java/
├── src/
│   └── main/
│       ├── java/
│       │   ├── Main.java                    # Entry point
│       │   └── com/compiler/
│       │       ├── Compila.java             # Main compiler with lexical & syntactic analysis
│       │       ├── Tlex.java                # Lexical analyzer (tokenizer only)
│       │       ├── TablaSimbolos.java       # Symbol table structure
│       │       └── TablaErrores.java        # Error tracking table
│       └── resources/
│           ├── Programa.txt                 # Sample program 1
│           ├── miPrograma1.txt              # Sample program 2
│           └── miPrograma2.txt              # Sample program 3
├── .gitignore
└── CLAUDE.md                                # This file
```

## Core Components

### 1. Main.java (Entry Point)
- Location: `src/main/java/Main.java`
- Creates a `Compila` instance with a source file
- Displays symbol table (tokens and occurrences)
- Displays error table if errors are found
- **Note**: Currently uses hardcoded Windows paths that need updating for cross-platform compatibility

### 2. Compila.java (Main Compiler)
- Location: `src/main/java/com/compiler/Compila.java`
- Combines lexical and syntactic analysis
- Manages token definitions (23 reserved words + operators + literals)
- Implements recursive descent parser with methods:
  - `Encabezado()`: Validates program header (PROGRAMA <id>;)
  - `Declara()`: Handles declarations (variables, constants, labels)
  - `Declara_Var()`: Variable declarations
  - `Tipo()`: Type checking (ENTERA, REAL)
  - `ListaVar()`: Variable lists
- Maintains symbol table and error table
- Uses buffered character input with 1000-character buffer

### 3. Tlex.java (Lexical Analyzer)
- Location: `src/main/java/com/compiler/Tlex.java`
- Pure tokenization without syntax validation
- Identical tokenization logic to Compila.java
- Builds symbol table with token frequencies
- Used for standalone lexical analysis

### 4. TablaSimbolos.java (Symbol Table)
- Location: `src/main/java/com/compiler/TablaSimbolos.java`
- Stores: word, type, occurrence count
- Static counter tracks total symbols
- `getTipoS()`: Returns human-readable token type descriptions
- Handles 23 reserved words + 17 operator/punctuation types

### 5. TablaErrores.java (Error Table)
- Location: `src/main/java/com/compiler/TablaErrores.java`
- Tracks: error number, offending word, line text, type, line number
- Static counter tracks total errors
- Used for syntax error reporting

## Token Type System

### Reserved Words (0-23)
```
TPROGRAMA=0, TETIQ=1, TVAR=2, TCONST=3, TENTERA=4, TREAL=5, TPI=6,
TLEE=7, TESCRIBE=8, TSI=9, TENTONCES=10, TSINO=11, TFINSI=12,
TMIENTRAS=13, THAZ=14, TFINM=15, TREPITE=16, THASTA=17,
TOR=18, TAND=19, TNOT=20, TSALTA=21, TID=22, TIMPRIME=23
```

### Literals (50-51)
```
TNUM_ENTERO=50, TNUM_REAL=51
```

### Operators & Punctuation (52-66)
```
TMAYOR=52, TMENOR=53, TMAY_IG=54, TMEN_IG=55, TDISTINTO=56, TIGUAL=57,
TASIGNACION=58, TPAR_IZQ=59, TPAR_DER=60, TARIT_MUL=61, TARIT_SUM=62,
TSTRING=63, TCHAR=64, TPUNTOYCOMA=65, TCOMA=66
```

### Error & EOF (97-99)
```
TERRORCADENA=97, TERROR=98, TEOF=99
```

## Development Guidelines for AI Assistants

### Understanding the Code

1. **Dual Implementation**: Both `Compila.java` and `Tlex.java` contain similar lexical analysis logic. `Compila.java` adds syntactic analysis on top of tokenization.

2. **Character Encoding**: Source files use ISO-8859-1/Windows-1252 encoding (note: "análisis" appears as "an�lisis"). When modifying comments, be aware of encoding issues.

3. **Parser State**: The parser maintains state through:
   - `iToken`: Current token type
   - `token`: Current token text (StringBuffer)
   - `cc`: Current character
   - `buffer`: Character buffer (1000 chars)

### Common Modification Patterns

#### Adding New Reserved Words
1. Define token constant (e.g., `TNUEVAPALABRA = 24`)
2. Add to `reservadas` array initialization in constructor
3. Update `TablaSimbolos.getTipoS()` for human-readable output
4. Add corresponding syntax rules if needed

#### Adding New Syntax Rules
1. Create new boolean method (e.g., `private boolean NuevaRegla()`)
2. Follow pattern: call `nextToken()`, validate, recurse if needed
3. Call `ValidaToken()` for each processed token
4. Use `insertaTError()` for syntax errors
5. Return `true` on success, `false` on error

#### Extending Token Recognition
In `nextToken()` method:
1. Add new `else if` branch after character type detection
2. Build token character by character using `token.append(cc)`
3. Call `next_char()` to advance
4. Return appropriate token type constant

### Testing Approach

1. **Sample Programs**: Use files in `src/main/resources/`:
   - `Programa.txt`: Contains intentional errors for testing
   - `miPrograma1.txt`, `miPrograma2.txt`: Additional test cases

2. **Main.java Configuration**: Update file paths in Main.java:
   ```java
   // Replace hardcoded paths with relative paths
   String file = "src/main/resources/Programa.txt";
   ```

3. **Compilation**: Standard Java compilation (no build tool currently)
   ```bash
   javac src/main/java/Main.java src/main/java/com/compiler/*.java
   java -cp src/main/java Main
   ```

### Code Quality Considerations

1. **Path Handling**: Main.java uses absolute Windows paths - should use relative paths or resource loading
2. **Buffer Size**: Fixed 1000-char buffer may be insufficient for large programs
3. **Error Recovery**: Parser stops on first major error rather than continuing
4. **Symbol Table**: Array-based with fixed size (MAX_BUF=1000) - no overflow protection
5. **Case Sensitivity**: Reserved words are case-insensitive (`compareToIgnoreCase`)
6. **Token Types**: Some constants are out of sequence (TIMPRIME=23, TID=22)

### Debugging Tips

1. **Token Stream**: Add debug output in `nextToken()` to trace tokenization:
   ```java
   System.out.println("Token: " + token + " Type: " + returnType);
   ```

2. **Parser State**: Track parser method calls to understand syntax validation flow

3. **Symbol Table Inspection**: Main.java already prints symbol table - examine token frequencies

4. **Error Messages**: Error table contains descriptive Spanish messages - useful for understanding failures

### File Modification Guidelines

1. **Preserve Encoding**: Be cautious with Spanish characters (á, é, í, ó, ú, ñ)
2. **Maintain Consistency**: If modifying lexer, update both `Compila.java` and `Tlex.java` if both should have the change
3. **Update Token Constants**: Keep token type documentation in sync with code
4. **Test Incrementally**: Test with sample programs after each change

### Known Issues to Address

1. **Hardcoded Paths**: Main.java:10-12 contain absolute Windows paths
2. **Incomplete Syntax Rules**: `Declara_Etiq()` and `Declara_Const()` are stubs
3. **No Build Configuration**: No Maven pom.xml or Gradle build file
4. **Limited Error Recovery**: Parser doesn't attempt to continue after certain errors
5. **Fixed Array Sizes**: No dynamic growth for symbol/error tables

### Working with Git

- Current branch: `claude/claude-md-mhxzwzsnbhjzpa7v-01S4PqaBpJRicVkAgGEeRx8N`
- Recent commits focus on code updates
- Standard .gitignore excludes compiled classes and IDE files

### Extending the Compiler

To add new language features:

1. **New Statement Type**:
   - Add reserved word(s)
   - Create parser method
   - Integrate into `Declara()` or create new entry point
   - Add error messages

2. **New Operator**:
   - Add token constant
   - Update `nextToken()` recognition logic
   - Update operator precedence if implementing expression parsing

3. **Type System**:
   - Currently minimal (ENTERA, REAL)
   - Would need type checking in symbol table
   - Add type attribute to `TablaSimbolos`

### Resources for Understanding

- Sample program syntax: `src/main/resources/Programa.txt`
- Token examples in comments throughout `Compila.java`
- Symbol table output when running Main.java shows token classification

## Quick Reference Commands

```bash
# Compile all Java files
javac src/main/java/Main.java src/main/java/com/compiler/*.java

# Run compiler on sample program
java -cp src/main/java Main

# Find all Java source files
find src -name "*.java"

# Search for specific token type usage
grep -r "TPROGRAMA" src/
```

## Contact and Contribution

When modifying this codebase:
- Test with all three sample programs in resources/
- Ensure symbol table output is correct
- Verify error detection and reporting
- Maintain backward compatibility with existing syntax
- Document new features in code comments

---

*Last Updated: 2025-11-13*
*Generated by: Claude Code Assistant*
