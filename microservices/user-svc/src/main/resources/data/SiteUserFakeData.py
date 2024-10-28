import csv
import requests
import argparse
from faker import Faker
from multiprocessing import Pool, cpu_count
from tqdm import tqdm

# Initialize Faker
fake = Faker()

# Function to generate a random request body
def generate_request():
    test = fake.items()
    request_data = {
        "emailAddress": fake.email(),
        "phoneNumber": fake.phone_number(),
        "firstName": fake.first_name(),
        "lastName": fake.last_name(),
        "password": fake.password(length=10)
    }
    return request_data

# Function to send POST request to API endpoint
def send_post_request(api_endpoint, data):
    try:
        response = requests.post(api_endpoint, json=data)

        if response.status_code == 409:
            print(f"Conflict (409): {response.text} - Continuing to next request...")
            return True  # Continue on 409
        else:
            print(f"Stopping program. Status: {response.status_code} - {response.text}")
            return False  # Stop on any other status code
    except Exception as e:
        print(f"Error occurred: {e}")
        return False  # Stop on error

# Function to save data to a single CSV file
def save_data_to_csv(data, filename="output.csv"):
    fieldnames = ["emailAddress", "phoneNumber", "firstName", "lastName", "password"]

    # Write to CSV file
    with open(filename, mode='w', newline='') as file:
        writer = csv.DictWriter(file, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(data)

    print(f"Data saved to {filename}")

# Function to load data from a CSV file
def load_data_from_csv(filename):
    data = []
    with open(filename, mode='r') as file:
        reader = csv.DictReader(file)
        for row in reader:
            data.append(row)
    return data

# Worker function to generate and send a single request
def worker(api_endpoint):
    request_data = generate_request()
    if not send_post_request(api_endpoint, request_data):
        print("Stopping the process due to non-409 status code.")

# Worker function to generate a single request (for data-only option)
def generate_worker(_):
    return generate_request()

# Generate and send the data to the specified API endpoint
def main(api_endpoint, num_requests=1, data_only=False, from_file=None):
    if from_file:
        # Load data from CSV file
        generated_requests = load_data_from_csv(from_file)
        print(f"Loaded {len(generated_requests)} requests from {from_file}")
    else:
        if data_only:
            # Generate new data in parallel with progress bar
            with Pool(cpu_count()) as pool:
                generated_requests = list(tqdm(pool.imap(generate_worker, range(num_requests)), total=num_requests, desc="Generating requests"))
            # Save the generated data to a single CSV file
            save_data_to_csv(generated_requests)
        else:
            # Generate new data
            generated_requests = [generate_request() for _ in range(num_requests)]
            with Pool(cpu_count()) as pool:
                list(tqdm(pool.starmap(worker, [(api_endpoint,) for _ in range(num_requests)]), total=num_requests, desc="Sending requests"))

if __name__ == "__main__":
    # Setup command-line argument parsing
    parser = argparse.ArgumentParser(
        description="Generate random requests or use data from a file and optionally send them to an API endpoint.")

    # Add arguments
    parser.add_argument("api_endpoint", type=str, nargs='?', default=None,
                        help="The API endpoint to send POST requests to.")
    parser.add_argument("--num_requests", type=int, default=1,
                        help="The number of requests to generate and send (default: 1).")
    parser.add_argument("--data-only", action="store_true",
                        help="If set, generate only data and save to CSV without sending requests.")
    parser.add_argument("--from-file", type=str, help="CSV file to load data from instead of generating new data.")

    # Parse the arguments
    args = parser.parse_args()

    # If --from-file is used, no need to generate new data
    if args.from_file:
        main(api_endpoint=args.api_endpoint, from_file=args.from_file)
    elif args.data_only:
        main(api_endpoint=None, num_requests=args.num_requests, data_only=True)
    else:
        if not args.api_endpoint:
            print("Error: API endpoint must be provided unless using --data-only or --from-file.")
        else:
            main(api_endpoint=args.api_endpoint, num_requests=args.num_requests)