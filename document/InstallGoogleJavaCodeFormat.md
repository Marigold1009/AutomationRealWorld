# ☕ Setting Up Google Java Code Format in IntelliJ IDEA (Windows + macOS)

This guide walks you through installing and configuring the **Google Java Format** plugin in IntelliJ IDEA, including necessary VM options to ensure compatibility with newer JDK versions.

---

## 🔧 Step 1: Install the Google Java Format Plugin

1. Open **IntelliJ IDEA**
2. Navigate to:
    - `File` → `Settings` → `Plugins` (on **Windows/Linux**), or
    - `IntelliJ IDEA` → `Preferences` → `Plugins` (on **macOS**)
3. Go to the `Marketplace` tab
4. Search for **Google Java Format**
5. Click **Install**
6. Restart the IDE

---

## ⚙️ Step 2: Configure the Plugin

1. Open:
    - `File` → `Settings` → `Other Settings` → `Google Java Format`
2. Check the option:
    - ✅ **Enable Google Java Format**

---

## 💻 Step 3: Edit Custom VM Options

To ensure compatibility with **JDK 16+**, you must add VM options that export internal `javac` modules.

### On **Windows**:

1. From the top menu, go to `Help` → `Edit Custom VM Options`
2. If prompted to create a copy, choose **Yes**
3. Add the following lines **at the end of the file**:

   ```ini
   --add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED
   --add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED
   --add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED
   --add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED
   --add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED
   --add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
   ```

4. Save and **restart IntelliJ**

---

## 🧪 Step 4: Format Your Code

You can format code using:

- `Ctrl + Alt + L` on **Windows/Linux**
- `Cmd + Option + L` on **macOS**

Or: Right-click in the editor → `Reformat Code`

---

## ✅ Verification

1. Open any `.java` file
2. Format it using the shortcut or menu
3. Check for 2-space indentation and consistent styling per the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

---

## 📌 Notes

- Google Java Format **overrides** IntelliJ’s default formatter
- To temporarily switch back to default formatting, disable the plugin
- Without the VM options above, you might encounter `InaccessibleObjectException` when formatting on JDK 16+