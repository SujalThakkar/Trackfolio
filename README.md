Trackfolio - Personal Skill & Certificate Tracker
================================================

A pure-Java desktop app built with Swing + SQLite.
Track skills, upload certificates, visualize progress, share portfolios.
Zero external libraries. Just run with Java 17+.

Features
--------
- Secure login / registration
- Add coding (rating) or non-coding (level) skills
- Upload JPG/PNG certificates
- Custom progress line-graph (Graphics2D)
- Real-time dashboard refresh
- View any user's portfolio by username
- Clean DAO + MVC-style architecture

Tech Stack
----------
Language : Java 17+
GUI      : Swing (JFrame, JPanel, ActionListener)
DB       : SQLite via JDBC
Graphics : Java 2D
File I/O : java.nio.file

Quick Start
-----------
git clone https://github.com/SujalThakkar/Trackfolio.git
cd trackfolio
javac -d out $(find src -name "*.java")
java -cp out com.trackfolio.Main

Sample Users
------------
username/password: user/user
username/password: sujal/1234

Future Ideas
------------
PDF export | Cloud sync | Dark mode | Web version

Resume Highlights
-----------------
- Full desktop app in pure Java
- Custom data visualization
- DAO pattern + PreparedStatement
- Responsive Swing UI
- Robust file handling
  

Happy coding! :)
