# OurLove API Backend

## Local Development
```bash
mvn spring-boot:run
```

## Deploy to Render

1. Push code to GitHub
2. Go to [Render Dashboard](https://dashboard.render.com)
3. New → Web Service → Connect GitHub repo
4. Settings:
   - **Root Directory**: (leave empty)
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/*.jar`
5. Add environment variables:
   - `DATABASE_URL` - PostgreSQL connection string
   - `CLOUDINARY_CLOUD_NAME`
   - `CLOUDINARY_API_KEY`
   - `CLOUDINARY_API_SECRET`

## Environment Variables

| Variable | Description |
|----------|-------------|
| `DATABASE_URL` | PostgreSQL URL |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name |
| `CLOUDINARY_API_KEY` | Cloudinary API key |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret |

## API Documentation
After deploy, access Swagger UI at: `https://your-app.onrender.com/swagger-ui.html`
