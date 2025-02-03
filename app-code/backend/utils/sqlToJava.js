const fs = require('fs');

const dataTypes = {
  varchar: "String",
  char: "String",
  serial: "Integer",
  int: "Integer",
  date: "Date"
};

const attributeTypes = {
  private: "private",
  public: "public"
};

// const classHeader = "@Data\n@Builder\n@AllArgsConstructor\n@NoArgsConstructor\n@Entity";
const classHeader = ["@Data", "@Builder", "@AllArgsConstructor", "@NoArgsConstructor", "@Entity"]
const imports = [
  "",
  "import jakarta.persistence.Entity;",
  "import jakarta.persistence.GeneratedValue;",
  "import jakarta.persistence.GenerationType;",
  "import jakarta.persistence.Id;",
  "import jakarta.persistence.Table;",
  "import lombok.AllArgsConstructor;",
  "import lombok.Builder;",
  "import lombok.Data;",
  "import lombok.NoArgsConstructor;",
  ""
]
const classCreateEnd = "\n}"
const attribute = attributeTypes.private;


const sqlNotInclude = ["CONSTRAINT", "PRIMARY KEY", "FOREIGN KEY", "REFERENCES"]
const fileName = "query.txt";
let start = 0

function lineFindWord(line, word) {
  return line.split(" ").includes(word);
}

function getClassName(word) {
  return word.split("_").map(([first, ...rest]) => {
    return [first.toUpperCase(), ...rest].join("")
  }).join('')
}

function lineFindWordSQL(line, words) {
  ["CONSTRAINT", "PRIMARY KEY", "FOREIGN KEY"].forEach(word => {
    if (line.startsWith(word)) {
      return true;
    }
  })
  return false;
}

function getLineClass(line) {
  if (!lineFindWordSQL(line, sqlNotInclude)) {
    return line;
  }
  return;
}

function saveFile(textArray, outputFileName) {
  const outputText = textArray.join('\n');
  fs.writeFile(outputFileName, outputText, 'utf-8', (err) => {
    if (err) {
      console.error(`Error writing file: ${err}`);
    } else {
      console.log(`File has been saved as ${outputFileName}`);
    }
  });
}

let classCreate = []
let classNames = []

fs.readFile(fileName, 'utf-8', (err, text) => {
  if (err) {
    // console.error(err)
    return;
  } else {
    const lines = text.split('\n');
    lines.forEach(line => {
      let className = "Default"
      // let classCreate = `${classHeader}`

      const trimLine = line.trim()
      if (lineFindWord(trimLine, "CREATE")) {
        if (start >= 1) {
          classCreate.push("}")
          saveFile(classCreate, classNames[classNames.length - 1] + ".java")
          console.log(classCreate)
          classCreate = []
        }
        className = getClassName(trimLine.split(" ").at(-2))
        classNames.push(className)
        // classCreate = `${classCreate}\npublic class ${className} {\n  @Id`
        classCreate.push("package com.sanfernando.sanfernando.models;")
        imports.forEach(importTxt =>
          classCreate.push(importTxt)
        )
        classHeader.forEach(header =>
          classCreate.push(header)
        )
        classCreate.push(`public class ${className} {`)
        classCreate.push('  @Id')

        start = start + 1
      } else {
        if (trimLine != "") {
          if (!(
            trimLine.startsWith('CONSTRAINT') ||
            trimLine.startsWith('PRIMARY KEY') ||
            trimLine.startsWith('FOREIGN KEY') ||
            trimLine.startsWith('REFERENCES') ||
            trimLine.startsWith('EXISTS') ||
            trimLine.startsWith(')')
          )) {
            const parts = trimLine.split(" ");
            for (const type in dataTypes) {
              if (trimLine.split(" ").map(part => {
                return part.toLowerCase()
              }).join(" ").includes(type)) {
                const lineClass = `  ${attribute} ${dataTypes[type]} ${parts[0]};`
                // classCreate = classCreate + "\n" + lineClass
                classCreate.push(lineClass)
                // console.log(lineClass)
              }
            }
          }
        }
      }
    })
  }
})

