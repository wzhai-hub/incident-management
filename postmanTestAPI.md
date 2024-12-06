1. Create Incident
   http://localhost:9090/api/incidents
```json
    {
  "accidentId": "integer",
  "title": "string",
  "description": "string",
  "location": "string",
  "severity": 1,
  "status": "string"
}
  ```
```json
[
    {
        "accidentId": 1,
        "title": "Car Collision",
        "description": "A collision between two cars on the highway, one car overturned.",
        "location": "Highway 5, near Exit 12",
        "severity": 3,
        "status": "Ongoing"
    },
    {
        "accidentId": 2,
        "title": "Motorcycle Accident",
        "description": "A motorcycle lost control on a curve and crashed into the guardrail.",
        "location": "Mountain Road, km 25",
        "severity": 2,
        "status": "Under Investigation"
    },
    {
        "accidentId": 3,
        "title": "Pedestrian Hit by Car",
        "description": "A pedestrian was hit by a car at a crosswalk. The driver fled the scene.",
        "location": "Main Street, near City Park",
        "severity": 4,
        "status": "Reported"
    },
    {
        "accidentId": 4,
        "title": "Truck Fire",
        "description": "A truck caught fire on the freeway, blocking two lanes.",
        "location": "Freeway 10, mile marker 33",
        "severity": 5,
        "status": "Cleared"
    }
]
```

