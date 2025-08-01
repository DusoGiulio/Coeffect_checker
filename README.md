## **Project: Coeffects for MiniJava**
This extension of MiniJava introduces coeffects that allow to control the use of parameters and local variables of methods.

## **Repository**
In the [source_code](./source_code) folder you'll find:
- The source code in the [src](./source_code/src) folder
- The documentation in the form of Javadoc in the [doc](./source_code/doc) folder
- The file specifying the grammar of the extension of MiniJAva, named [`miniJava.g4`](./source_code/CfMj.g4)

In the [executable_code](./executable_code) folder you'll find:
- The executable file [`Coeffect_MiniJava.jar`](./executable_code/Coeffect_MiniJava.jar)
- The folder  [predef_coeff](./executable_code/predef_coeff) with the file `CoeffettiBase.java`
- The folder [examples](./executable_code/examples) with the files `CoeffInference1.java` and `CoeffInference2.java` with examples that you can run.

## **How to Use the Executable File**
1. Download the [executable_code](./executable_code).
2. The folder from which you start  `Coeffect_MiniJava.jar` should contain a subfolder `predef_coeff` with the file `CoeffettiBase.java`.
3. `java -jar  Coeffect_MiniJava.jar` starts a GUI:
   - Press the "Aggiungi File" button.
   - Select the .java or .txt files you want to analyze. **Attention, the first selected file must contain the main method.**
   - Press the "Avvia analisi" button.
   - The results will be displayed in the text area below the buttons.
   - In the subdirectory `Output` you can the files produced by the coeffect checker.

# Authors

* **[Giulio Duso] ** - [University of Eastern Piedmont](https://unict.it](https://www.uniupo.it/en)
* **[Paola Giannini]([https://github.com/fmes](https://people.unipmn.it/giannini/))** - [University of Eastern Piedmont](https://unict.it](https://www.uniupo.it/en)
