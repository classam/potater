package net.lassam.potater

import org.scalatra._
import scalate.ScalateSupport

class MyScalatraServlet extends PotaterStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

  get("/login"){
    <html>
      <body>
        <h1>Authentication code! </h1>
      </body>
    </html>
  }

  get("/users"){
    <html>
      <body>
        <h1>Redirect to current user-page or login.</h1>
      </body>
    </html>
  }
  
}
