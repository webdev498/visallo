# Visallo Configuration

Visallo loads `.properties` files for configuration
from Visallo `/config`<sup id="a1">[1](#f1)</sup> directories.

* files within each location are loaded alphabetically
* values set later can override earlier values
* values can be referenced when setting other values using `${other.property}`

Visallo loads plugin `.jar` files from Visallo
`/lib`<sup id="a1">[1](#f1)</sup> directories.

* include one (1) web authentication plugin
* include any desired web, Graph Property Worker, and other plugins

---
<sup id="f1">1</sup> {% include '../VISALLO_DIR.md' %} [&crarr;](#a1)
