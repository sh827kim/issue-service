dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    implementation("at.favre.lib:bcrypt:0.9.0")
    runtimeOnly("io.r2dbc:r2dbc-h2")
}
