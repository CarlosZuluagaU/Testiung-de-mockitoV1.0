const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
    // URL base para los tests
    baseUrl: "http://localhost:8080",
    
    // Tiempo de espera por defecto
    defaultCommandTimeout: 5000,
    requestTimeout: 5000,
    
    // Viewport
    viewportWidth: 1280,
    viewportHeight: 720,
    
    // Video y screenshots
    video: false,
    screenshotOnRunFailure: true,
    
    // Configuración de navegador
    chromeWebSecurity: false,
  },

  // Configuración global
  reporter: "cypress-mochawesome-reporter",
  reporterOptions: {
    reportDir: "cypress/reports",
    overwrite: true,
    html: true,
    json: true,
  },
});
