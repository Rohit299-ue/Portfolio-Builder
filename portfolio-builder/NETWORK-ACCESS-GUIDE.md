# How to Access Portfolio Builder from Another Laptop

## Option 1: Local Network Access (Same WiFi)

### Step 1: Find Your IP Address

**On the laptop running the application:**

**Windows:**
```cmd
ipconfig
```
Look for "IPv4 Address" under your WiFi adapter (e.g., 192.168.1.5)

**Mac/Linux:**
```bash
ifconfig
```
Look for "inet" address (e.g., 192.168.1.5)

### Step 2: Start the Application

1. Run `START-APP.bat` on your laptop
2. Wait for "Started PortfolioApplication"
3. Note your IP address (e.g., 192.168.1.5)

### Step 3: Configure Firewall

**Windows Firewall:**
1. Open Windows Defender Firewall
2. Click "Advanced settings"
3. Click "Inbound Rules" → "New Rule"
4. Select "Port" → Next
5. Select "TCP" → Specific local ports: 8080
6. Select "Allow the connection" → Next
7. Check all profiles (Domain, Private, Public) → Next
8. Name: "Portfolio Builder" → Finish

**Or use this command (Run as Administrator):**
```cmd
netsh advfirewall firewall add rule name="Portfolio Builder" dir=in action=allow protocol=TCP localport=8080
```

### Step 4: Access from Another Laptop

**On the other laptop (must be on same WiFi):**

Open browser and go to:
```
http://YOUR_IP_ADDRESS:8080
```

Example:
```
http://192.168.1.5:8080
```

### Troubleshooting:

**If it doesn't work:**

1. **Check both laptops are on same WiFi network**
2. **Disable firewall temporarily to test:**
   ```cmd
   netsh advfirewall set allprofiles state off
   ```
   (Re-enable after testing: `netsh advfirewall set allprofiles state on`)

3. **Check application is running:**
   - Look for "Started PortfolioApplication" message

4. **Verify port 8080 is open:**
   ```cmd
   netstat -ano | findstr :8080
   ```

---

## Option 2: Internet Access (Deploy to Cloud)

To access from anywhere via internet, you need to deploy to cloud:

### A. Deploy to Heroku (Free Tier)

**Step 1: Install Heroku CLI**
```bash
# Download from: https://devcenter.heroku.com/articles/heroku-cli
```

**Step 2: Login to Heroku**
```bash
heroku login
```

**Step 3: Create Heroku App**
```bash
cd portfolio-builder
heroku create your-portfolio-builder
```

**Step 4: Add Procfile**
Create file `Procfile` in portfolio-builder folder:
```
web: java -jar target/portfolio-builder-1.0.0.jar --server.port=$PORT
```

**Step 5: Deploy**
```bash
git init
git add .
git commit -m "Initial commit"
git push heroku master
```

**Step 6: Access**
```
https://your-portfolio-builder.herokuapp.com
```

### B. Deploy to Railway (Easier)

**Step 1: Go to Railway.app**
```
https://railway.app
```

**Step 2: Sign up with GitHub**

**Step 3: New Project → Deploy from GitHub**

**Step 4: Select your repository**

**Step 5: Railway auto-deploys**

**Step 6: Get your URL**
```
https://your-app.railway.app
```

### C. Use ngrok (Quick Testing)

**Step 1: Download ngrok**
```
https://ngrok.com/download
```

**Step 2: Start your application**
```
START-APP.bat
```

**Step 3: In another terminal, run ngrok**
```cmd
ngrok http 8080
```

**Step 4: Get public URL**
```
Forwarding: https://abc123.ngrok.io → http://localhost:8080
```

**Step 5: Share the URL**
```
https://abc123.ngrok.io
```

**Note:** ngrok free tier URL changes every time you restart.

---

## Quick Setup for Local Network Access

### On Host Laptop (Running Application):

1. **Find IP Address:**
   ```cmd
   ipconfig
   ```
   Note: IPv4 Address (e.g., 192.168.1.5)

2. **Allow Port 8080 in Firewall:**
   ```cmd
   netsh advfirewall firewall add rule name="Portfolio Builder" dir=in action=allow protocol=TCP localport=8080
   ```

3. **Start Application:**
   ```cmd
   START-APP.bat
   ```

4. **Share this URL with others:**
   ```
   http://192.168.1.5:8080
   ```
   (Replace 192.168.1.5 with your actual IP)

### On Other Laptop (Accessing):

1. **Connect to same WiFi**
2. **Open browser**
3. **Go to:** `http://192.168.1.5:8080`
4. **Use the application!**

---

## Configuration for Network Access

### Update application.properties

Add this to `src/main/resources/application.properties`:

```properties
# Allow access from any IP
server.address=0.0.0.0

# Port
server.port=8080
```

### Rebuild Application

```cmd
mvnw clean package
```

---

## Security Considerations

### For Local Network:
- ✅ Safe for same WiFi network
- ✅ Good for testing and demos
- ⚠️ Don't use on public WiFi

### For Internet Access:
- ⚠️ Add HTTPS (SSL certificate)
- ⚠️ Implement rate limiting
- ⚠️ Add stronger authentication
- ⚠️ Use production database (PostgreSQL)
- ⚠️ Enable CORS properly

---

## Common Issues and Solutions

### Issue 1: Cannot connect from other laptop

**Solution:**
1. Check both on same WiFi
2. Disable firewall temporarily
3. Verify IP address is correct
4. Check application is running

### Issue 2: Connection refused

**Solution:**
1. Check port 8080 is not blocked
2. Verify application started successfully
3. Check firewall rules

### Issue 3: Slow performance

**Solution:**
1. Check WiFi signal strength
2. Reduce image sizes
3. Close unnecessary applications

---

## Best Practices

1. **For Demo/Presentation:**
   - Use local network access
   - Test before presentation
   - Have backup plan (localhost)

2. **For Production:**
   - Deploy to cloud (Heroku, Railway, AWS)
   - Use custom domain
   - Enable HTTPS
   - Use production database

3. **For Development:**
   - Use localhost (http://localhost:8080)
   - Test on local network before deploying

---

## Quick Reference

| Scenario | Solution | URL Format |
|----------|----------|------------|
| Same laptop | Localhost | http://localhost:8080 |
| Same WiFi | Local IP | http://192.168.1.5:8080 |
| Internet (ngrok) | ngrok tunnel | https://abc123.ngrok.io |
| Internet (cloud) | Deploy to cloud | https://yourapp.herokuapp.com |

---

## Summary

**Easiest for Demo:** Local Network Access
1. Find IP: `ipconfig`
2. Allow firewall: Port 8080
3. Start app: `START-APP.bat`
4. Share: `http://YOUR_IP:8080`

**Best for Production:** Cloud Deployment
1. Use Heroku or Railway
2. Deploy application
3. Get permanent URL
4. Share with anyone

---

**Need Help?**
- Local network: Check firewall and WiFi
- Cloud deployment: Follow platform documentation
- ngrok: Quick testing, URL changes each time
